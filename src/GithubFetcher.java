import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GithubFetcher {
    public static void main(String[] args) {

        if(args.length <1 ){
            System.out.println("You need to give a username :");
            return;
        }

        String URL = "https://api.github.com/users/"+args[0]+"/events";

        try{
            HttpResponse<String> response;
            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(URL))
                        .header("Accept", "application/vnd.github.v3+json")
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            if(response.statusCode() == 200){
                //System.out.println("Raw JSON Response:\n" + response.body());
                parseResponse(response.body());
            }

        }catch(Exception e){

        }
    }

    private static void parseResponse(String json){
        String[] temp = json.split("\\},\\{");
        for(String event : temp){
            if(event.contains("\"type\"")){
                //System.out.println("it got in the if");
                int typeIndex = event.indexOf("\"type\"");
                String eventType = event.substring(typeIndex + 8,event.indexOf("\"",typeIndex + 8));

                int repoIndex = event.indexOf("\"name\"");
                int repoNameIndex = event.indexOf("\"name\"",repoIndex);
                String repoName = event.substring(repoNameIndex+8,event.indexOf("\"",repoNameIndex+8));

                int timeIndex = event.indexOf("\"created_at\"");
                String time = event.substring(timeIndex + 14, event.indexOf("\"",timeIndex + 14));

                System.out.println("Event type: "+eventType);
                System.out.println("Repository name: "+repoName);
                System.out.println("Create at: "+time);
                System.out.println("===========");
            }

        }


    }
}