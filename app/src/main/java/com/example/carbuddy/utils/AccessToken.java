package com.example.carbuddy.utils;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AccessToken {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"car-buddy-e89b4\",\n" +
                    "  \"private_key_id\": \"dde919d7ecda0c295983930b92645168598572c1\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiQoSWXDKCgd7M\\nmWTc7vqcBDN7i4FJudbsiLKBrsR2RqDC4CeuTxGSK7QmBbcVE41YNdmdNpnvuvIi\\nAlhRE4xOf0IpQCK5SaE6mmdeT1jf51hvROzoJgjvT/jsRDjjMhqzoj8vBqBzBO1F\\nTiAHmDvI2VRk1j0LTg7n4FKuOyQEaLt0YeAwbVYKIEnqPKZJ/hXMn8fbKTZH0P7i\\nUu5oc+9FAAh2U3syQwYafP0qlP0bFYEz3H5I+hY5cXfdJA71o0iClZktdMH8BC0y\\n15Ei+YXpKbIXabkVvS+JPz5LJFO99R5MA3505Ehzu+sR0ZzVQeoZE+parS1qO6WX\\nqU9wd4bLAgMBAAECggEAGhjcvlpB0spp4o+2sJoh5en/EHCARjKvpgeDvmaKOVkO\\nuN6DQ1PQSFQ4e00RWesfy/+NP+3s6v6EZy09YHzmZNGu+yInnj7WFwOH0JwDzVn7\\ngpnxXeflfqgRfzP8elOFW+hNL+u+7quXhegU2rkvab8asG/iDZQWbaZKELUjtoQ9\\nbJW1RCdWlai1dNmMveaic/fRtX2NSqvnT7aBmVgSv+M0Ysqu8byxSesCiqkMW6ps\\nMssPVuFt48Gbn+Y1r2iyNJIc5GjrVde+CJjmhO5zyQxdOID1ObYsoE8mD1TXr3XM\\nczBZfjdVqDrEuu1LQWGLDD0yDu3zJQ/QqtA6CaRtcQKBgQDY7LxFvstvfb/CU8k2\\nktXegluxZBb/5HF2iqTRdryvuvZ8/gscQLhShiyEOXC0iL0qvG74Jf/RkegI+TmD\\nkqKZGw3CuZE4ykbnmdI+cfKadFxO49Gbd/UsjKVn4q1L5y3dYNUcRod2MWCJpwWv\\nbC9yVmKGKhy4FLyQzxEzv14fRQKBgQC/fPctsMekAqvfc8LQ3DOfScHm1L1FMp/o\\n0cAeRoiZUhj8EF+k5U9eNEbX+fnWcDYMR/DQz+n6LaD9g88T+CGQE3ER1FxkxyBI\\nz8HHQaf5FdIb26ORyd5Uyhb0YfNKP4g5I5rAUc0kpqKhkwgCO3Ek1YzCb5zkNA3p\\nLnKnP1gmzwKBgFz6CzBxuMyOv3RRaTsE8LowcZC07866YY+HmWFWXUWNAz1LvgNc\\nEiT88XOzEwWuXjZFEeM2llGDEktWVzp3zU2GdP1zgDUxLfbyzVSKjot5OF9v8lWK\\nswFnPzYpyFFyQwxSBsXZzRHx1AjsF9m6qxN8xznuLuUPrkr7FcpySatpAoGADCtf\\n9hpnBahZzfCob+eerD/ev9HDWZN3EC/MWuA2rC/IgLR1r6AXJDCEVBOB6jvlY3Br\\nAIpGH20o7xOiZG1/jI1/wRdS6+Cvz5DMiuJET5MwcC7MPWar542UWeGjuZNCEAeQ\\nPSq1s9+XNFeIycOHVETyFiVLvkEEpU4bYT+oVYcCgYEAi/RhZG4ozXhKSZGpBcjx\\nVtT7MPEW79jHq4C/qnPz+OkOBhFLqznBYxNJWOxoZrKs9uQBAyWFPXUwJ7opxPaa\\nP48pdVLTzS0OCIsHuUaxFDWrbIRWQ0T0pfH2oBz/2bXKNNv/5K/qDexcja6x4R0h\\nx1/rPW5ikGdGZKTWBTnebGs=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-on8x9@car-buddy-e89b4.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"107839949852501741861\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-on8x9%40car-buddy-e89b4.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList(firebaseMessagingScope));
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            Log.d("access token error", Objects.requireNonNull(e.getLocalizedMessage()));
            return null;
        }
    }
}
