package com.example.newsarena.Common;

import com.example.newsarena.Interface.IconBetterIdeaService;
import com.example.newsarena.Interface.NewsService;
import com.example.newsarena.Model.IconBetterIdea;
import com.example.newsarena.Remote.IconBetterIdeaClient;
import com.example.newsarena.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {
    private static final String BASE_URL="https://newsapi.org/";

    public static final String API_KEY="9d0d7af7da1146208fc079e4c73e82e8";

    public static NewsService getNewsService()
    {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService()
    {
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    public static String getAPIUrl(String source,String sortBy,String apiKEY){
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }
}
