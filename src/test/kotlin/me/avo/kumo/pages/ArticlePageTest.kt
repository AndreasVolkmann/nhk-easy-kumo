package me.avo.kumo.pages

import me.avo.getBodyFromHtmlFile
import me.avo.kumo.nhk.data.Headline
import me.avo.kumo.nhk.pages.ArticlePage
import me.avo.kumo.util.*
import org.amshove.kluent.*
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test

internal class ArticlePageTest {

    private val dummyArticlePage = ArticlePage(headline = Headline("x", "x", "x", "x"))

    @Test fun `new layout`() {
        val body = getBodyFromHtmlFile("new-layout-article.html")
        val content = dummyArticlePage.getContent(body)
        println(content)
        content.shouldNotBeBlank()

        dummyArticlePage.getImage(body) ?: null to null
    }

    @Test fun `Verify that there are no additional html tags in the content`() {
        val expectedContent = "東京都の多摩動物公園で、２月にチーターの赤ちゃんが同じお母さんから３匹生まれました。雄が２匹で、雌が１匹です。\n" +
                "いまチーターは世界で数が少なくなっていて、多摩動物公園はほかの動物園などと協力してチーターを増やしたいと考えています。３匹を大事に育ててきましたが、重さが３.５ｋｇぐらいになって丈夫になってきたため、４月からみんなに見せています。\n" +
                "３匹は元気に走ったり一緒に遊んだりしています。たくさんの人たちが「かわいい」などと言って見ていました。\n" +
                "多摩動物公園の人は「元気な３匹をぜひ見に来てください」と話していました。"
        val html = this::class.loadResource("Article_k10010959041000.html")
        val body = Jsoup.parse(html).body()
        val content = dummyArticlePage.getContent(body)
        println(content)

        content shouldNotStartWith "<"
        content shouldBeEqualTo expectedContent
    }

    @Test fun verifyHtmlTags() {
        val expectedContent = "製品の安全などを調べているＮＩＴＥによると、去年３月までの５年に、家のこんろや電子レンジなどの事故が９８９件ありました。２３人が火事で亡くなりました。\n" +
                "事故の３０％以上は、油などで汚れたこんろを使ったため火が出たり、近くにあった燃えやすい物に火が移ったりしました。こんろが汚れて火がつきにくいため、何度もつけようとして、出ていたガスが燃えてやけどをした人もいました。\n" +
                "電子レンジも油で汚れたままにしておくと、火が出る危険があります。\n" +
                "ＮＩＴＥは、こんろや電子レンジなどは使ったあとに掃除してほしいと言っています。そして、こんろの近くには燃えやすい物を置かないように言っています。"
        val html = this::class.loadResource("Article_k10010967861000.html")
        val body = Jsoup.parse(html).body()
        val content = dummyArticlePage.getContent(body)
        println(content)

        content shouldNotStartWith "<"
        content shouldBeEqualTo expectedContent
    }

    @Test fun `Content can contain urls`() {
        val expectedContent = "九州では５日からの雨で、山が崩れたり、道や家に水が入ったりして、大きな被害が出ています。\n" +
                "「震災がつなぐ全国ネットワーク」という団体は、このような水の被害があったあとしなければならないことをインターネットなどで紹介しています。\n" +
                "家などの写真は被害がよくわかるように撮ります。そして、市や町の役所に被害を伝えて、「り災証明書」をもらうことが大事です。保険会社や大家にも連絡します。ほかに、ぬれた家具や携帯電話などをどうするかとか、家の掃除のやり方なども紹介しています。\n" +
                "団体の人は「いま避難している人は、これから何をしなければならないのかわからなくて、心配だと思います。これを読んで少しでも安心してほしいです」と話しています。\n" +
                "下のブログから見ることができます。http://blog.canpan.info/shintsuna/"
        val html = this::class.loadResource("Article_k10011048511000.html")
        val body = Jsoup.parse(html).body()
        val content = dummyArticlePage.getContent(body)
        println(content)
        content shouldBeEqualTo expectedContent
    }

    @Test fun `Various tags`() {
        val input = "<p><span class=\"colorL\"><ruby>北京市<rt>ぺきんし</rt></ruby></span>は<ruby>３日<rt>みっか</rt></ruby>、<span class=\"colorL\"><ruby>北京市<rt>ぺきんし</rt></ruby></span>の<ruby>去年<rt>きょねん</rt></ruby>のＰＭ２．５の<a href=\"javascript:void(0)\" class=\"dicWin\" id=\"id-0003\"><ruby><span class=\"under\">平均</span><rt>へいきん</rt></ruby></a>の<a href=\"javascript:void(0)\" class=\"dicWin\" id=\"id-0000\"><ruby><span class=\"under\">濃度</span><rt>のうど</rt></ruby></a>が１m<sup>3</sup>に５８マイクログラム（０．００００５８ｇ）になったと<a href=\"javascript:void(0)\" class=\"dicWin\" id=\"id-0004\"><ruby><span class=\"under\">発表</span><rt>はっぴょう</rt></ruby></a>しました。５<ruby>年<rt>ねん</rt></ruby><ruby>前<rt>まえ</rt></ruby>より３５．６％<ruby>下<rt>さ</rt></ruby>がりました。</p>"
        input.getText() shouldContain "濃度が１m3に５８マイクログラム"
    }

}