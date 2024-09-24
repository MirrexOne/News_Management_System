package dev.mirrex.serviceTests;

import dev.mirrex.entities.Tag;
import dev.mirrex.repositories.TagRepository;
import dev.mirrex.services.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrCreateTags_whenTagsExistAndNewTagsCreated() {
        List<String> tagNames = Arrays.asList("Tag1", "Tag2", "Tag3");

        Tag existingTag = new Tag();
        existingTag.setTitle("Tag1");

        when(tagRepository.findByTitleIn(anyList())).thenReturn(new ArrayList<>(List.of(existingTag)));

        doAnswer(invocation -> null).when(tagRepository).saveAll(anyList());

        Set<Tag> result = tagService.getOrCreateTags(tagNames);

        assertEquals(3, result.size()); // Должно вернуться 3 тега (1 существующий и 2 новых)

        verify(tagRepository, times(1)).saveAll(argThat(tags -> {
            Set<String> tagTitles = new HashSet<>();
            for (Tag tag : tags) {
                tagTitles.add(tag.getTitle());
            }
            return tagTitles.containsAll(Set.of("Tag2", "Tag3"));
        }));

        verify(tagRepository, times(1)).findByTitleIn(tagNames);
    }

    @Test
    void testGetOrCreateTags_whenAllTagsExist() {
        List<String> tagNames = Arrays.asList("Tag1", "Tag2");

        Tag tag1 = new Tag();
        tag1.setTitle("Tag1");

        Tag tag2 = new Tag();
        tag2.setTitle("Tag2");

        when(tagRepository.findByTitleIn(anyList())).thenReturn(new ArrayList<>(List.of(tag1, tag2)));

        Set<Tag> result = tagService.getOrCreateTags(tagNames);

        assertEquals(2, result.size());

        verify(tagRepository, never()).saveAll(anyList());

        verify(tagRepository, times(1)).findByTitleIn(tagNames);
    }
}
