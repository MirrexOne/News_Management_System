package dev.mirrex.services.impl;

import dev.mirrex.entities.Tag;
import dev.mirrex.repositories.TagRepository;
import dev.mirrex.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Set<Tag> getOrCreateTags(List<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByTitleIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> {
                    Tag tag = new Tag();
                    tag.setTitle(name);
                    return tag;
                })
                .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
        }

        existingTags.addAll(newTags);
        return new HashSet<>(existingTags);
    }
}
