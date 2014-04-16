package com.example.WhoISscanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseHelper {
    public ArrayList<HashMap<String, String>> getPage(String url) {

        //url = "http://who.is/whois/" + url;
        url = "http://www.whois-search.com/whois/" + url;
        StringBuilder builder = new StringBuilder();

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                builder.append(s).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //return builder.toString();

        HashMap<String, String> map;
        //Pattern p = Pattern.compile("person:\\\\s+(.+)\\n", Pattern.CASE_INSENSITIVE);
        Pattern p = Pattern.compile("person:\\s+(.*)\\n", Pattern.CASE_INSENSITIVE);
        String str = builder.toString();
        Matcher matcher = p.matcher(str);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        if (matcher.find()) {
            map = new HashMap<String, String>();
            map.put("param", "person: ");
            map.put("value", matcher.group(1));
            list.add(map);
        }

        p = Pattern.compile("created:\\s+(.*)\\n", Pattern.CASE_INSENSITIVE);
        matcher = p.matcher(str);
        if (matcher.find()) {
            map = new HashMap<String, String>();
            map.put("param", "created: ");
            map.put("value", matcher.group(1));
            list.add(map);
        }

        p = Pattern.compile("city:\\s+(.*)\\n", Pattern.CASE_INSENSITIVE);
        matcher = p.matcher(str);
        if (matcher.find()) {
            map = new HashMap<String, String>();
            map.put("param", "city: ");
            map.put("value", matcher.group(1));
            list.add(map);
        }

        return  list;
    }
}
