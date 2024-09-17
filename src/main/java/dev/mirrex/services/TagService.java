package dev.mirrex.services;

import dev.mirrex.entities.Tag;
import java.util.List;
import java.util.Set;

public interface TagService {

    Set<Tag> getOrCreateTags(List<String> tagNames);
}
