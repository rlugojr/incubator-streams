package com.datasift.test;

import org.apache.streams.datasift.Wikipedia;
import org.apache.streams.datasift.blog.Blog;
import org.apache.streams.datasift.board.Board;
import org.apache.streams.datasift.config.Facebook;
import org.apache.streams.datasift.provider.DatasiftEventClassifier;
import org.apache.streams.datasift.youtube.YouTube;
import org.apache.streams.twitter.pojo.Delete;
import org.apache.streams.twitter.pojo.Retweet;
import org.apache.streams.twitter.pojo.Tweet;
import org.apache.streams.twitter.provider.TwitterEventClassifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sblackmon on 12/13/13.
 */
public class DatasiftEventClassifierTest {

    //private String twitter = "{\"created_at\":\"Wed Dec 11 22:27:34 +0000 2013\",\"id\":410898682381615105,\"id_str\":\"410898682381615105\",\"text\":\"Men's Basketball Single-Game Tickets Available - A limited number of tickets remain for Kentucky's upcoming men's ... http:\\/\\/t.co\\/SH5YZGpdRx\",\"source\":\"\\u003ca href=\\\"http:\\/\\/www.hootsuite.com\\\" rel=\\\"nofollow\\\"\\u003eHootSuite\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":91407775,\"id_str\":\"91407775\",\"name\":\"Winchester, KY\",\"screen_name\":\"winchester_ky\",\"location\":\"\",\"url\":null,\"description\":null,\"protected\":false,\"followers_count\":136,\"friends_count\":0,\"listed_count\":1,\"created_at\":\"Fri Nov 20 19:29:02 +0000 2009\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":1793,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/613854495\\/winchester_sociallogo_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/613854495\\/winchester_sociallogo_normal.jpg\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[{\"url\":\"http:\\/\\/t.co\\/SH5YZGpdRx\",\"expanded_url\":\"http:\\/\\/ow.ly\\/2C2XL1\",\"display_url\":\"ow.ly\\/2C2XL1\",\"indices\":[118,140]}],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"medium\",\"lang\":\"en\"}\n";
    private String facebook = "{\"facebook\":{\"author\":{\"avatar\":\"https:\\/\\/graph.facebook.com\\/100000529773118\\/picture\",\"id\":\"100000529773118\",\"link\":\"http:\\/\\/www.facebook.com\\/profile.php?id=100000529773118\",\"name\":\"Ghatholl Perkasa\"},\"caption\":\"jakartagreater.com\",\"created_at\":\"Thu, 03 Oct 2013 00:48:32 +0000\",\"description\":\"Pangdam VI Mulawarman menjelaskan, tahun 2014 mendapatkan dukungan 2 unit tank Leopard-2, serta 2 unit Helikopter Apache, menemani Helikopter Bell 412 EP\",\"id\":\"100000529773118_709380572422929\",\"link\":\"http:\\/\\/jakartagreater.com\\/helikopter-apache-dan-tank-leopard-temani-bell-412-ep-di-kalimantan\\/\",\"message\":\"http:\\/\\/jakartagreater.com\\/helikopter-apache-dan-tank-leopard-temani-bell-412-ep-di-kalimantan\\/\",\"name\":\"Helikopter Apache dan Tank Leopard Temani Bell 412 EP di Kalimantan | JakartaGreater\",\"source\":\"web\",\"type\":\"link\"},\"interaction\":{\"author\":{\"avatar\":\"https:\\/\\/graph.facebook.com\\/100000529773118\\/picture\",\"id\":\"100000529773118\",\"link\":\"http:\\/\\/www.facebook.com\\/profile.php?id=100000529773118\",\"name\":\"Ghatholl Perkasa\"},\"content\":\"http:\\/\\/jakartagreater.com\\/helikopter-apache-dan-tank-leopard-temani-bell-412-ep-di-kalimantan\\/\",\"created_at\":\"Thu, 03 Oct 2013 00:49:02 +0000\",\"id\":\"1e32bc586b7fa000e066c7349fcb4420\",\"link\":\"http:\\/\\/www.facebook.com\\/100000529773118_709380572422929\",\"schema\":{\"version\":3},\"source\":\"web\",\"subtype\":\"link\",\"title\":\"Helikopter Apache dan Tank Leopard Temani Bell 412 EP di Kalimantan | JakartaGreater\",\"type\":\"facebook\"},\"links\":{\"code\":[200],\"created_at\":[\"Wed, 02 Oct 2013 14:42:46 +0000\"],\"meta\":{\"charset\":[\"UTF-8\"],\"content_type\":[\"text\\/html\"],\"description\":[\"Pangdam VI Mulawarman menjelaskan, tahun 2014 mendapatkan dukungan 2 unit tank Leopard-2, serta 2 unit Helikopter Apache, menemani Helikopter Bell 412 EP\"],\"keywords\":[[\"Helikopter Apache Kalimantan\"]],\"lang\":[\"en-us\"]},\"normalized_url\":[\"http:\\/\\/jakartagreater.com\\/helikopter-apache-dan-tank-leopard-temani-bell-412-ep-di-kalimantan\"],\"retweet_count\":[0],\"title\":[\"Helikopter Apache dan Tank Leopard Temani Bell 412 EP di Kalimantan | JakartaGreater\"],\"url\":[\"http:\\/\\/jakartagreater.com\\/helikopter-apache-dan-tank-leopard-temani-bell-412-ep-di-kalimantan\\/\"]},\"salience\":{\"content\":{\"sentiment\":0},\"title\":{\"sentiment\":0}}}\n";
    private String youtube = "{\"interaction\":{\"author\":{\"link\":\"http:\\/\\/youtube.com\\/y_4c0uy7ikisinxqjnquva\",\"name\":\"y_4c0uy7ikisinxqjnquva\"},\"content\":\"comenten y subo otros.\",\"contenttype\":\"html\",\"created_at\":\"Thu, 03 Oct 2013 01:54:22 +0000\",\"id\":\"1e32bc7c8499aa80e0612c55f4551676\",\"link\":\"http:\\/\\/www.youtube.com\\/watch?v=Yj6ckGTNJ7M\",\"schema\":{\"version\":3},\"title\":\"FA! Fuerte Apache NO TE CONFUNDAS 2013] DEMO\",\"type\":\"youtube\"},\"language\":{\"confidence\":62,\"tag\":\"es\"},\"salience\":{\"content\":{\"sentiment\":7},\"title\":{\"sentiment\":0}},\"youtube\":{\"author\":{\"link\":\"http:\\/\\/youtube.com\\/y_4c0uy7ikisinxqjnquva\",\"name\":\"y_4c0uy7ikisinxqjnquva\"},\"category\":\"People &#38; Blogs\",\"content\":\"comenten y subo otros.\",\"contenttype\":\"html\",\"created_at\":\"Thu, 03 Oct 2013 01:04:41 +0000\",\"duration\":\"219\",\"id\":\"1e32bc7c8499aa80e0612c55f4551676\",\"title\":\"FA! Fuerte Apache NO TE CONFUNDAS 2013] DEMO\",\"type\":\"video\",\"videolink\":\"http:\\/\\/www.youtube.com\\/watch?v=Yj6ckGTNJ7M\"}}\n";
    private String board = "{\"board\":{\"anchor\":\"post8881971\",\"author\":{\"avatar\":\"http:\\/\\/www.gstatic.com\\/psa\\/static\\/1.gif\",\"link\":\"http:\\/\\/forums.techguy.org\\/members\\/496500-phantom010.html\",\"location\":\"Cyberspace\",\"name\":\"Phantom010\",\"registered\":\"Sun, 01 Mar 2009 00:00:00 +0000\",\"username\":\"Phantom010\"},\"boardname\":\"Tech Support Guy - Free help for Windows 7, Vista, XP, and more!\",\"categories\":\"Software\",\"content\":\"<div class=\\\"KonaBody\\\"><div style=\\\"margin:20px; margin-top:5px; \\\"> <div class=\\\"smallfont\\\" style=\\\"margin-bottom:2px\\\">Quote:<\\/div> <table cellpadding=\\\"6\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" width=\\\"100%\\\"> <tr> <td class=\\\"alt2\\\" style=\\\"border:1px inset\\\"> <div>\\n\\t\\t\\t\\t\\tOriginally Posted by <strong>donsor<\\/strong> <a href=\\\"http:\\/\\/forums.techguy.org\\/all-other-software\\/1122437-apache-open-office.html#post8881771\\\" rel=\\\"nofollow\\\"><img class=\\\"inlineimg sprite-viewpost\\\" pagespeed_lazy_src=\\\"http:\\/\\/attach.tsgstatic.com\\/tsg\\/styles\\/common\\/blank.png\\\" width=\\\"12\\\" height=\\\"12\\\" alt=\\\"View Post\\\" src=\\\"http:\\/\\/www.gstatic.com\\/psa\\/static\\/1.gif\\\" onload=\\\"pagespeed.lazyLoadImages.loadIfVisible(this);\\\"\\/><\\/a> <\\/div> <div style=\\\"font-style:italic\\\">I tried downloading Libreoffice but encountered some problem. No big deal I'll find something else.<\\/div> <\\/td> <\\/tr> <\\/table> <\\/div>What kind of problem?<\\/div>\",\"contenttype\":\"html\",\"countrycode\":\"US\",\"crawled\":\"Sun, 23 Mar 2014 09:18:13 +0000\",\"created_at\":\"Sat, 22 Mar 2014 16:03:00 -0500\",\"domain\":\"www.techguy.org\",\"forumid\":\"469bbbf115a\",\"forumname\":\"All Other Software\",\"forumurl\":\"http:\\/\\/forums.techguy.org\\/18-all-other-software\\/\",\"gmt\":\"-5\",\"id\":\"1e3b20559a7daa00e072105aaa3de61e\",\"language\":\"English\",\"link\":\"http:\\/\\/forums.techguy.org\\/all-other-software\\/1122437-apache-open-office.html#post8881971\",\"siteid\":\"3cbc7f773\",\"thread\":\"http:\\/\\/forums.techguy.org\\/all-other-software\\/1122437-apache-open-office.html\",\"threadid\":\"1122437\",\"title\":\"Apache Open Office\",\"topics\":\"Computers\",\"type\":\"post\"},\"interaction\":{\"author\":{\"avatar\":\"http:\\/\\/www.gstatic.com\\/psa\\/static\\/1.gif\",\"link\":\"http:\\/\\/forums.techguy.org\\/members\\/496500-phantom010.html\",\"location\":\"Cyberspace\",\"name\":\"Phantom010\",\"registered\":\"Sun, 01 Mar 2009 00:00:00 +0000\",\"username\":\"Phantom010\"},\"content\":\"<div class=\\\"KonaBody\\\"><div style=\\\"margin:20px; margin-top:5px; \\\"> <div class=\\\"smallfont\\\" style=\\\"margin-bottom:2px\\\">Quote:<\\/div> <table cellpadding=\\\"6\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" width=\\\"100%\\\"> <tr> <td class=\\\"alt2\\\" style=\\\"border:1px inset\\\"> <div>\\n\\t\\t\\t\\t\\tOriginally Posted by <strong>donsor<\\/strong> <a href=\\\"http:\\/\\/forums.techguy.org\\/all-other-software\\/1122437-apache-open-office.html#post8881771\\\" rel=\\\"nofollow\\\"><img class=\\\"inlineimg sprite-viewpost\\\" pagespeed_lazy_src=\\\"http:\\/\\/attach.tsgstatic.com\\/tsg\\/styles\\/common\\/blank.png\\\" width=\\\"12\\\" height=\\\"12\\\" alt=\\\"View Post\\\" src=\\\"http:\\/\\/www.gstatic.com\\/psa\\/static\\/1.gif\\\" onload=\\\"pagespeed.lazyLoadImages.loadIfVisible(this);\\\"\\/><\\/a> <\\/div> <div style=\\\"font-style:italic\\\">I tried downloading Libreoffice but encountered some problem. No big deal I'll find something else.<\\/div> <\\/td> <\\/tr> <\\/table> <\\/div>What kind of problem?<\\/div>\",\"contenttype\":\"html\",\"created_at\":\"Sat, 22 Mar 2014 16:03:00 -0500\",\"id\":\"1e3b20559a7daa00e072105aaa3de61e\",\"link\":\"http:\\/\\/forums.techguy.org\\/all-other-software\\/1122437-apache-open-office.html#post8881971\",\"schema\":{\"version\":3},\"subtype\":\"post\",\"title\":\"Apache Open Office\",\"type\":\"board\"},\"language\":{\"confidence\":99,\"tag\":\"en\",\"tag_extended\":\"en\"},\"salience\":{\"content\":{\"sentiment\":-4},\"title\":{\"sentiment\":0}}}";
    private String blog = "{\"blog\":{\"author\":{\"name\":\"Stileex\"},\"blog\":{\"link\":\"http:\\/\\/laptop.toprealtime.net\\/\",\"title\":\"Laptop News\"},\"blogid\":\"71107731\",\"content\":\"If you are a gaming freak and play video games even when you are on the move, you need a gaming laptop that gives a smooth and fast performance and has great graphics and audio output. MSI has come up with a small and lightweight laptop with stunning\\u00A0\\u2026\",\"contenttype\":\"html\",\"created_at\":\"Sat, 22 Mar 2014 21:35:23 +0000\",\"domain\":\"laptop.toprealtime.net\",\"guid\":\"8921b89d1faad3d22a6e2a7ed55765e1\",\"id\":\"1e3b209dfc61af80e072ce1ee22bb088\",\"lang\":\"en\",\"link\":\"http:\\/\\/laptop.toprealtime.net\\/2014\\/msi-ge60-apache-pro-003-15-6%e2%80%b3-excellent-gaming-laptop-pressandupdate\\/\",\"parseddate\":\"Sun, 23 Mar 2014 00:21:59 +0000\",\"postid\":\"ed8205a9-2812-4fa6-a730-692b75adc9d7\",\"title\":\"MSI GE60 Apache Pro 003 15.6\\u2033: Excellent Gaming Laptop \\u2013 PressAndUpdate\",\"type\":\"post\"},\"interaction\":{\"author\":{\"name\":\"Stileex\"},\"content\":\"If you are a gaming freak and play video games even when you are on the move, you need a gaming laptop that gives a smooth and fast performance and has great graphics and audio output. MSI has come up with a small and lightweight laptop with stunning\\u00A0\\u2026\",\"contenttype\":\"html\",\"created_at\":\"Sat, 22 Mar 2014 21:35:23 +0000\",\"id\":\"1e3b209dfc61af80e072ce1ee22bb088\",\"link\":\"http:\\/\\/laptop.toprealtime.net\\/2014\\/msi-ge60-apache-pro-003-15-6%e2%80%b3-excellent-gaming-laptop-pressandupdate\\/\",\"received_at\":4743640105474628098,\"schema\":{\"version\":3},\"subtype\":\"post\",\"title\":\"MSI GE60 Apache Pro 003 15.6\\u2033: Excellent Gaming Laptop \\u2013 PressAndUpdate\",\"type\":\"blog\"},\"language\":{\"confidence\":99,\"tag\":\"en\",\"tag_extended\":\"en\"},\"salience\":{\"content\":{\"entities\":[{\"name\":\"MSI\",\"sentiment\":0,\"confident\":1,\"label\":\"Company\",\"evidence\":2,\"type\":\"Company\",\"about\":1,\"themes\":[\"fast performance\",\"great graphics\",\"audio output\",\"gaming freak\",\"gaming laptop\",\"lightweight laptop\",\"stunning \\u2026\"]}],\"sentiment\":2,\"topics\":[{\"name\":\"Video Games\",\"score\":0.64548820257187,\"additional\":\"If you are a gaming freak and play video games even when you are on the move, you need a gaming laptop that gives a smooth and fast performance and has great graphics and audio output. MSI has come up with a small and lightweight laptop with stunning \\u2026\",\"hits\":0},{\"name\":\"Hardware\",\"score\":0.45651730895042,\"additional\":\"If you are a gaming freak and play video games even when you are on the move, you need a gaming laptop that gives a smooth and fast performance and has great graphics and audio output. MSI has come up with a small and lightweight laptop with stunning \\u2026\",\"hits\":0}]},\"title\":{\"sentiment\":6,\"topics\":[{\"name\":\"Video Games\",\"score\":0.62834107875824,\"additional\":\"MSI GE60 Apache Pro 003 15.6\\u2033: Excellent Gaming Laptop \\u2013 PressAndUpdate\",\"hits\":0}]}}}";

//    @Test
//    public void testDetectTwitter() {
//        Class result = TwitterEventClassifier.detectClass(twitter);
//        if( !result.equals(Tweet.class) )
//            Assert.fail();
//    }

    @Test
    public void testDetectFacebook() {
        Class result = DatasiftEventClassifier.detectClass(facebook);
        if( !result.equals(Facebook.class) )
            Assert.fail();
    }

    @Test
    public void testDetectYoutube() {
        Class result = DatasiftEventClassifier.detectClass(youtube);
        if( !result.equals(YouTube.class) )
            Assert.fail();
    }

    @Test
    public void testDetectBoard() {
        Class result = DatasiftEventClassifier.detectClass(board);
        if( !result.equals(Board.class) )
            Assert.fail();
    }

    @Test
    public void testDetectBlog() {
        Class result = DatasiftEventClassifier.detectClass(blog);
        if( !result.equals(Blog.class) )
            Assert.fail();
    }

}
