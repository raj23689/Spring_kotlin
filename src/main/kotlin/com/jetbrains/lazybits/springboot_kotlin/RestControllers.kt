package com.jetbrains.lazybits.springboot_kotlin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/v1/articles")
class ArticleController(val articleRepository: ArticleRepository) {

    @GetMapping
    fun articles(): Iterable<Article> = articleRepository.findAllByOrderByCreatedAtDesc();

    @GetMapping("/{slug}")
    fun articleByTitle(@PathVariable slug: String) =
        articleRepository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

    @PostMapping
    fun newArticle(@RequestBody article: Article): Article {
        article.id = null
        articleRepository.save(article)
        return article
    }

    @PutMapping("/{slug}")
    fun updateArticle(@PathVariable slug: String, @RequestBody article: Article): Article {
        val existingArticle =
            articleRepository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        existingArticle.content = article.content;
        articleRepository.save(existingArticle)
        return existingArticle
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(@PathVariable slug: String) {
        val existingArticle =
            articleRepository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        articleRepository.delete(existingArticle)
    }

}