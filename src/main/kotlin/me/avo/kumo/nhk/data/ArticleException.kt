package me.avo.kumo.nhk.data

class ArticleException(val articleId: String, exception: Exception) : Exception(exception) {

    constructor(article: Article, exception: Exception) : this(article.id, exception)

    constructor(headline: Headline, exception: Exception) : this(headline.id, exception)

}

