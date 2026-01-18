package org.reda.rag_simplevs.dataLoaderRag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class DataLoader {

    @Value("classpath:/pdfs/speach.pdf")
    private Resource pdfFile;

    @Value("speach_To_vector")
    private String vectorStore;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore sVectorStore = SimpleVectorStore.builder(embeddingModel).build(); // instancier SimpleVectorStore//        instancier SimpleVectoreStore dans le quel on va enregistrer notre fichier
        String path = Path.of("src","main","resources", "store")
                .toAbsolutePath()
                +"/"+ vectorStore;// extraction de Path ou se trouve notre base de donnée vectorielle
        File file = new File(path); // creer le fichier de base de donnee vectorielle avec le path qu'on a deja extrait
        if(file.exists()){ // si la base existe
            sVectorStore.load(file);
        }else{ // si la base vectorielle n'existe pas on doit la creer
            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfFile); //importer le PDF
            List<Document> documents = pdfDocumentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);
            sVectorStore.accept(chunks);
            sVectorStore.save(file);
        }
        return sVectorStore;
    }
}
