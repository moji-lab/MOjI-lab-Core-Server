package com.moji.server.service;

import com.moji.server.domain.TourApiAddress;
import com.moji.server.model.DefaultRes;
import com.moji.server.util.StatusCode;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.json.XML;

import javax.swing.text.html.Option;

@Service
public class TourApiService {


    // 핵심 주소 조회(Tour Api, Addres 테이블)
    public DefaultRes getAddressByKeyword(String keyword) {
        try{
            List<TourApiAddress> tourApiAddresses  = new ArrayList<>();
            if(this.getSubAddressAndLatAndLnByGooglePlaceApi(keyword).isPresent()){
                JSONArray jsonArray = this.getSubAddressAndLatAndLnByGooglePlaceApi(keyword).get();
                for(int i = 0; i<jsonArray.length(); i++){
                    String subAddress = jsonArray.getJSONObject(i).getString("formatted_address");
                    TourApiAddress tourApiAddress = new TourApiAddress();

                    String mainAddress = jsonArray.getJSONObject(i).getString("name");
                    String lat = String.format("%.6f",jsonArray.getJSONObject(i).getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lat"));
                    String lng = String.format("%.6f",jsonArray.getJSONObject(i).getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lng"));

                    tourApiAddress.setMainAddress(mainAddress);
                    tourApiAddress.setSubAddress(subAddress);
                    tourApiAddress.setLat(lat);
                    tourApiAddress.setLng(lng);
                    tourApiAddresses.add(tourApiAddress);
                }
            }
            else{
                return DefaultRes.res(StatusCode.NOT_FOUND, "키워드에 해당되는 주소가 없습니다.");
            }
            return DefaultRes.res(StatusCode.OK, "주소 조회 성공", tourApiAddresses);
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }

    public Optional<String> getSubAddressByGoogleMapGeoCodingApi(final String lat, final String lng) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("latlng", "UTF-8") + "=" +
                    URLEncoder.encode(lat + "," + lng, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" +
                    URLEncoder.encode("AIzaSyBgVeh-rQgjqNogc95eeINu1CExY6B-EoM", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("language", "UTF-8") + "=" +
                    URLEncoder.encode("ko", "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            JSONObject jsonObject = new JSONObject(sb.toString());
            return Optional.ofNullable(jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<JSONArray> getSubAddressAndLatAndLnByGooglePlaceApi(final String keyword) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("query", "UTF-8") + "=" +
                    URLEncoder.encode(keyword, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" +
                    URLEncoder.encode("AIzaSyBgVeh-rQgjqNogc95eeINu1CExY6B-EoM", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("region", "UTF-8") + "=" +
                    URLEncoder.encode("kr", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("language", "UTF-8") + "=" +
                    URLEncoder.encode("ko", "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            JSONObject jsonObject = new JSONObject(sb.toString());
            return Optional.ofNullable(jsonObject.getJSONArray("results"));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
