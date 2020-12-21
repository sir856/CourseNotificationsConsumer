package com.kpfu.consumer.course_notifications_consumer.controllers;

import com.kpfu.consumer.course_notifications_consumer.model.Interest;
import com.kpfu.consumer.course_notifications_consumer.model.Knowledge;
import com.kpfu.consumer.course_notifications_consumer.model.Notification;
import com.kpfu.consumer.course_notifications_consumer.model.Tag;
import com.kpfu.consumer.course_notifications_consumer.repositories.InterestsRepository;
import com.kpfu.consumer.course_notifications_consumer.repositories.KnowledgeRepository;
import com.kpfu.consumer.course_notifications_consumer.repositories.TagsRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/interest")
@CrossOrigin
public class InterestController {

    private final KnowledgeRepository knowledgeRepository;

    private final TagsRepository tagsRepository;

    private final InterestsRepository interestsRepository;

    private final SimpMessagingTemplate template;

    @Autowired
    public InterestController(KnowledgeRepository knowledgeRepository, TagsRepository tagsRepository, InterestsRepository interestsRepository, SimpMessagingTemplate template) {
        this.knowledgeRepository = knowledgeRepository;
        this.tagsRepository = tagsRepository;
        this.interestsRepository = interestsRepository;
        this.template = template;
    }

    @PostMapping("/knowledge/add")
    public List<Knowledge> addKnowledge(@RequestBody Set<String> knowledgeNames) {
        List<Knowledge> knowledgeEntities = new LinkedList<>();
        for (String knowledgeName : knowledgeNames) {
            Knowledge knowledge = new Knowledge();
            knowledge.setName(knowledgeName);
            knowledgeEntities.add(knowledge);
        }

        return knowledgeRepository.saveAll(knowledgeEntities);
    }

    @PostMapping("/tag/add")
    public List<Tag> addTags(@RequestBody Set<String> tagNames) {
        List<Tag> tagEntities = new LinkedList<>();
        for (String knowledgeName : tagNames) {
            Tag tag = new Tag();
            tag.setName(knowledgeName);
            tagEntities.add(tag);
        }

        return tagsRepository.saveAll(tagEntities);
    }

    @PostMapping("/add")
    public List<Interest> addInterests(@RequestBody Map<Integer, Set<Integer>> interestsMap) {
        List<Interest> interests = new LinkedList<>();

        for (Integer knowledgeId : interestsMap.keySet()) {
            Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
                    .orElseThrow(() -> new IllegalArgumentException("No knowledge with id = " + knowledgeId));

            for (Integer tagId : interestsMap.get(knowledgeId)) {
                Tag tag = tagsRepository.findById(tagId)
                        .orElseThrow(() -> new IllegalArgumentException("No tag with id = " + tagId));
                Interest interest = new Interest();
                interest.setKnowledge(knowledge);
                interest.setTag(tag);
                interests.add(interest);
            }
        }

        return interestsRepository.saveAll(interests);
    }

    @GetMapping(value = "/knowledge", produces = "application/json")
    public String getKnowledgeList() {
        List<Knowledge> knowledgeList = knowledgeRepository.findAll();
        JSONArray knowledgeJsonArray = new JSONArray();

        for (Knowledge knowledge : knowledgeList) {
            JSONObject knowledgeJson = new JSONObject();
            knowledgeJson.put("id", knowledge.getId());
            knowledgeJson.put("name", knowledge.getName());

            JSONArray tagsJsonArray = new JSONArray();

            for (Interest interest : knowledge.getInterests()) {
                JSONObject tagJson = new JSONObject();
                tagJson.put("id", interest.getTag().getId());
                tagJson.put("name", interest.getTag().getName());
                tagsJsonArray.put(tagJson);
            }

            knowledgeJson.put("tags", tagsJsonArray);

            knowledgeJsonArray.put(knowledgeJson);
        }

        return knowledgeJsonArray.toString();
    }

    @PutMapping("/notification")
    public Notification notification(@RequestBody Notification notification) {
        template.convertAndSend("/course/notification", notification);

        return notification;
    }
}
