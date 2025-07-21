package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "llama3";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Đọc tin nhắn người dùng
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            sendErrorResponse(response, "Không thể đọc tin nhắn người dùng.");
            return;
        }

        String userMsg = new JSONObject(sb.toString()).optString("message", "");
        if (userMsg.trim().isEmpty()) {
            sendErrorResponse(response, "Tin nhắn không được để trống.");
            return;
        }

        // Gửi yêu cầu đến Ollama
        String botResponse;
        try {
            botResponse = callOllamaAPI(userMsg);
        } catch (Exception e) {
            sendErrorResponse(response, "Lỗi khi liên kết với Ollama: " + e.getMessage());
            return;
        }

        // Trả về phản hồi JSON
        JSONObject json = new JSONObject();
        json.put("reply", botResponse);
        response.getWriter().write(json.toString());
    }

    private String callOllamaAPI(String userMsg) throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("model", MODEL);
        payload.put("prompt", userMsg);
        payload.put("stream", true); // Kích hoạt streaming

        HttpURLConnection conn = (HttpURLConnection) new URL(OLLAMA_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.toString().getBytes("UTF-8"));
        }

        StringBuilder ollamaResponse = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                JSONObject chunk = new JSONObject(line);
                if (chunk.optBoolean("done", false)) break;
                ollamaResponse.append(chunk.optString("response", ""));
            }
        } catch (IOException e) {
            throw new IOException("Lỗi khi đọc phản hồi từ Ollama: " + e.getMessage());
        } finally {
            conn.disconnect();
        }

        return ollamaResponse.toString().trim();
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JSONObject json = new JSONObject();
        json.put("error", message);
        response.getWriter().write(json.toString());
    }
}