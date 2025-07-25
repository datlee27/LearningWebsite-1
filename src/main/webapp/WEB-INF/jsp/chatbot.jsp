<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#chatbot-btn {
    position: fixed;
    bottom: 32px;
    right: 32px;
    width: 64px;
    height: 64px;
    background: #007bff;
    color: #fff;
    border-radius: 50%;
    border: none;
    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
    font-size: 2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1050;
    cursor: pointer;
    transition: background 0.2s;
}
#chatbot-btn:hover {
    background: #0056b3;
}
#chatbot-popup {
    position: fixed;
    bottom: 110px;
    right: 32px;
    width: 350px;
    max-width: 90vw;
    height: 450px;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.18);
    display: none;
    flex-direction: column;
    z-index: 1051;
    border: 1px solid #ddd;
}
#chatbot-popup.active {
    display: flex;
}
#chatbot-header {
    background: #007bff;
    color: #fff;
    padding: 12px 16px;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    font-weight: 600;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
#chatbot-messages {
    flex: 1;
    padding: 16px;
    overflow-y: auto;
    font-size: 0.97rem;
}
#chatbot-messages .user {
    text-align: right;
    margin: 5px 0;
    color: #007bff;
}
#chatbot-messages .bot {
    text-align: left;
    margin: 5px 0;
    color: #333;
}
#chatbot-input-area {
    display: flex;
    border-top: 1px solid #eee;
    padding: 8px;
    background: #fafbfc;
}
#chatbot-input {
    flex: 1;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 6px 10px;
    font-size: 1rem;
}
#chatbot-send {
    background: #007bff;
    color: #fff;
    border: none;
    border-radius: 8px;
    margin-left: 8px;
    padding: 6px 16px;
    font-size: 1rem;
    cursor: pointer;
    transition: background 0.2s;
}
#chatbot-send:hover {
    background: #0056b3;
}
</style>

<!-- Chatbot Button -->
<button id="chatbot-btn" title="Hỏi AI">
    <i class="bi bi-robot"></i>
</button>

<!-- Chatbot Popup -->
<div id="chatbot-popup">
    <div id="chatbot-header">
        <span>Chatbot AI</span>
        <button type="button" id="chatbot-close" style="background:none;border:none;color:#fff;font-size:1.3rem;">×</button>
    </div>
    <div id="chatbot-messages">
        <div class="bot">Xin chào! Hỏi tôi bất cứ điều gì về trang web hoặc khóa học của bạn.</div>
    </div>
    <form id="chatbot-input-area" autocomplete="off">
        <input type="text" id="chatbot-input" placeholder="Nhập câu hỏi..." required />
        <button type="submit" id="chatbot-send">Gửi</button>
    </form>
</div>

<script>
function escapeHTML(str) {
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}

document.getElementById('chatbot-btn').onclick = function() {
    document.getElementById('chatbot-popup').classList.add('active');
};
document.getElementById('chatbot-close').onclick = function() {
    document.getElementById('chatbot-popup').classList.remove('active');
};
document.getElementById('chatbot-input-area').onsubmit = async function(e) {
    e.preventDefault();
    const input = document.getElementById('chatbot-input');
    const msg = input.value.trim();
    if (!msg) return;
    const messages = document.getElementById('chatbot-messages');
    messages.innerHTML += `<div class="user"><b>Bạn:</b> ${escapeHTML(msg)}</div>`;
    input.value = '';
    messages.scrollTop = messages.scrollHeight;

    messages.innerHTML += `<div class="bot" style="color:#888;"><i>AI đang trả lời...</i></div>`;
    messages.scrollTop = messages.scrollHeight;
    try {
        const res = await fetch('${pageContext.request.contextPath}/chatbot', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({message: msg})
        });
        if (!res.ok) throw new Error('Lỗi kết nối với AI');
        const data = await res.json();
        if (data.error) throw new Error(data.error);
        messages.innerHTML = messages.innerHTML.replace('<div class="bot" style="color:#888;"><i>AI đang trả lời...</i></div>', '');
        messages.innerHTML += `<div class="bot"><b>AI:</b> ${escapeHTML(data.reply)}</div>`;
        messages.scrollTop = messages.scrollHeight;
    } catch (err) {
        messages.innerHTML = messages.innerHTML.replace('<div class="bot" style="color:#888;"><i>AI đang trả lời...</i></div>', '');
        messages.innerHTML += `<div class="bot" style="color:red;">Lỗi: ${escapeHTML(err.message)}</div>`;
        messages.scrollTop = messages.scrollHeight;
    }
};
</script>