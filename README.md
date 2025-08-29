 🎓 Learning Management System (LMS) with AI Assistant  

## 👥 Nhóm thực hiện  
- Đặng Phương Khôi Nguyên 
- Lê Văn Đạt  

**Lớp:** SE18B02  

---

## 📖 Giới thiệu  
Trong bối cảnh Cách mạng công nghiệp 4.0, giáo dục trực tuyến (E-learning) ngày càng phổ biến.  
Các hệ thống **LMS (Learning Management System)** không chỉ giúp quản lý khóa học, người dùng, tài nguyên học tập, mà còn hỗ trợ việc giảng dạy và học tập hiệu quả hơn.  

Đề tài **“Xây dựng Website Hệ thống Quản lý Học tập (LMS) Tích hợp Trợ lý AI”** tập trung vào:  
- Xây dựng hệ thống quản lý học tập trực tuyến theo mô hình **MVC** bằng Java.  
- Hỗ trợ 3 vai trò: **Student, Teacher, Admin**.  
- Tích hợp **AI Assistant (Gemini API)** để hỗ trợ học viên trong quá trình học tập.  

---

## 🚀 Tính năng chính  

### 👨‍🎓 Student  
- Đăng ký / đăng nhập / đăng xuất  
- Xem danh sách khóa học và chi tiết khóa học  
- Tham gia học, xem video bài giảng, tài liệu  
- Nộp bài tập và theo dõi tiến độ  
- Hỏi đáp, tương tác với **AI Assistant**  

### 👨‍🏫 Teacher  
- Tạo, chỉnh sửa, xóa khóa học  
- Quản lý bài giảng, bài tập trong khóa học  
- Xem danh sách học viên tham gia  
- Chấm điểm và phản hồi bài tập  

### 🛠️ Admin  
- Quản lý toàn bộ tài khoản người dùng (thêm, sửa, xóa, khóa)  
- Quản lý tất cả các khóa học trên hệ thống  
- Xem báo cáo, thống kê cơ bản  

### 🤖 AI Assistant  
- Tích hợp **Gemini API**  
- Hỗ trợ học viên 24/7 trong việc học tập  
- Giải đáp câu hỏi, gợi ý khóa học, giải thích nội dung  

---

## 🛠️ Công nghệ sử dụng  
- **Backend**: Java 17, Jakarta EE (Servlets, JPA, Hibernate), JDBC  
- **Frontend**: JSP, JSTL, Bootstrap 5, JavaScript (ES6+)  
- **Database**: MySQL 8.0  
- **AI Integration**: Google Gemini API  
- **Server**: Apache Tomcat 10  
- **Build Tool**: Maven  

---

## 📂 Cấu trúc dự án  
LMS/
┣ src/ # Java source code (Controller, DAO, Model)

┣ web/ # JSP pages (View)

┣ database/ # SQL scripts for database schema

┣ README.md

┗ pom.xml # Maven build file

📸 ScreenShots

<img width="806" height="413" alt="Ảnh màn hình 2025-08-29 lúc 15 07 32" src="https://github.com/user-attachments/assets/16786393-5de0-4aa1-84e7-9641a38b6667" />


🔑 Login & Register

<img width="810" height="423" alt="Ảnh màn hình 2025-08-29 lúc 15 07 40" src="https://github.com/user-attachments/assets/05e47b47-ed7f-4a24-8800-096b2da59d8b" />

![Uploading Ảnh màn hình 2025-08-29 lúc 15.08.05.png…]()




📚 Course List

<img width="808" height="420" alt="Ảnh màn hình 2025-08-29 lúc 15 07 58" src="https://github.com/user-attachments/assets/2b804d31-4012-441b-9fec-844ba873334e" />

📖 Course Detail & Lectures


<img width="809" height="441" alt="Ảnh màn hình 2025-08-29 lúc 15 08 13" src="https://github.com/user-attachments/assets/b1530d8e-4846-4a05-8bf9-167d53606039" />

<img width="800" height="422" alt="Ảnh màn hình 2025-08-29 lúc 15 08 38" src="https://github.com/user-attachments/assets/5343881f-e0fd-4bde-ba5f-2dd8cd7ad44d" />


👨‍🏫 Teacher Management

<img width="800" height="422" alt="Ảnh màn hình 2025-08-29 lúc 15 08 38" src="https://github.com/user-attachments/assets/d484bd36-71bd-4197-a35b-928fb5b959b2" />


🤖 AI Assistant

<img width="798" height="420" alt="Ảnh màn hình 2025-08-29 lúc 15 08 54" src="https://github.com/user-attachments/assets/9bda1c56-396f-4470-9d54-4a931ea38a9f" />


