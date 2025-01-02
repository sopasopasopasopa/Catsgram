package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.Comparator;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final Map<Long, Post> posts = new HashMap<>();

    public Optional<Post> findById(@PathVariable Long postId) {
        return posts.values().stream()
                .filter(x -> x.getId().equals(postId))
                .findFirst();
    }

    public Collection<Post> findAll(int from, int size, String sort) {
        List<Post> sortedPosts = posts.values().stream()
                .sorted("asc".equalsIgnoreCase(sort) ?
                        Comparator.comparing(Post::getPostDate) :
                        Comparator.comparing(Post::getPostDate).reversed())
                .collect(Collectors.toList());

        int toIndex = Math.min(from + size, sortedPosts.size());
        return sortedPosts.subList(from, toIndex);
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}