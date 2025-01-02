package ru.yandex.practicum.catsgram.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{postId}")
    public Optional<Post> findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @GetMapping("/posts/search")
    public List<Post> searchPosts(@RequestParam String author,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        System.out.println("Ищем посты пользователя с именем " + author + " и опубликованные " + date);
        return List.of();
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        if (size <= 0) {
            throw new ConditionsNotMetException("Параметр size должен быть больше нуля");
        }

        return postService.findAll(from, size, sort);
    }

    @PostMapping(value = "/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}