package com.yurakamri.ajrcollection.service;

import com.google.gson.Gson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.yurakamri.ajrcollection.entity.SmsForToken;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.payload.SMS.SMSDto;
import com.yurakamri.ajrcollection.repository.SmsForApiRepo;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsForApiRepo smsForApiRepo;

    public SmsForToken getSmsApi() {
        Long maxId = smsForApiRepo.maxId();
        return smsForApiRepo.getById(maxId);
    }

    public ApiResponse addSmsApi(SmsForToken smsForToken) {
        try {
            String token = getToken(smsForToken);
            smsForToken.setToken(token);
            smsForApiRepo.save(smsForToken);
            return new ApiResponse(true, "Muvaffaqqiyatli qo`shildi", smsForToken);
        } catch (Exception e) {
            return new ApiResponse(false, "Xatolik");
        }
    }

    public String getToken(SmsForToken smsForToken) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", smsForToken.getEmail())
                .addFormDataPart("password", smsForToken.getPassword())
                .build();
        Request request = new Request.Builder()
                .url(smsForToken.getUrl())
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SMSDto sms = gson.fromJson(str, SMSDto.class);
            return sms.getData().getToken();
        } catch (Exception e) {
            return null;
        }
    }

    public Response sendMessageCode(String phoneNumber, String code) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone", phoneNumber.substring(1))
                .addFormDataPart("message", "Ajr Collection. Tasdiqlash kodi: " + code)
                .addFormDataPart("from", "4546")
                .addFormDataPart("callback_url", "http://0000.uz/test.php")
                .addFormDataPart("user_sms_id", "sms1")
                .build();

        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/message/sms/send")
                .addHeader("Authorization", "Bearer " + getSmsApi().getToken())
                .method("POST", body)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<Object> send(String phoneNumber, String code) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        String uriStr = "https://rest.messagebird.com/messages";
        URI uri = new URI(uriStr);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "AccessKey pX4Ti0YqeoTUeCzX9ezBFpWT1");

        RequestEntity<Object> request = new RequestEntity<>(Map.of(
                "recipients", phoneNumber,
                "originator", "Xurshidbek",
                "body", "Ajrcollection code: " + code
        ), headers, HttpMethod.POST, uri);

        return restTemplate.exchange(request, Object.class);
    }


    public void send_sms(String phoneNumber, String sms) {

        try {
            HttpResponse response = Unirest.post("https://api.releans.com/v2/message")
                    .header("Authorization", "Bearer 86731b55771b0a6b25d8573a3900f5a9")
                    .field("sender", "BellaStella")
                    .field("mobile", "+" + phoneNumber)
                    .field("content", "AjrCollection. \n Tasdiqlash kodi: "+sms)
                    .asString();
//            response();
            sendMessageCode(phoneNumber, sms);
        } catch (Exception e) {
            System.out.println("sms yuborishda xatolik"+"  "+e);
        }


//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "sender=BellaStella&mobile="+phoneNumber+"&content="+sms);
//        Request request = new Request.Builder()
//                .url("https://api.releans.com/v2/message")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer 86731b55771b0a6b25d8573a3900f5a9")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    @Transactional

    public Response response(){
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    Request request = new Request.Builder()
            .url("https://api.releans.com/v2/balance")
            .method("GET", null)
            .addHeader("Authorization", "Bearer 86731b55771b0a6b25d8573a3900f5a9")
            .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body().toString());
            return response;


    }
}
