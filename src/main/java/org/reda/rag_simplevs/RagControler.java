package org.reda.rag_simplevs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.scheduler.Schedulers;

@RestController
public class RagControler {
    private ChatClient chatClient;
//    @Autowired
//    private VectorStore vectorStore;

    public RagControler(ChatClient.Builder builder ) {
        this.chatClient = builder.build();
//                .defaultAdvisors(
//                         QuestionAnswerAdvisor.builder(vectorStore)
//                                 .searchRequest(SearchRequest.builder().build())
//                                 .promptTemplate(new PromptTemplate(new ClassPathResource("prompt/prompt.st")))
//                                 .scheduler(Schedulers.boundedElastic())
//                                .build()
//                )
    }

    @GetMapping("/ask")
    public String ask(String question){
        return chatClient.prompt().user(question).call().content();
    }
}
