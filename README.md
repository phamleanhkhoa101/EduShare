# 🎓 EduShare (AcademicHub)
### AI-Powered Academic Document Sharing & Moderation Platform

[![Java](https://img.shields.io/badge/Java-25%20LTS-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-9.x-blue.svg)](https://gradle.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)

---

## 📖 1. Giới thiệu tổng quan (Overview)

**EduShare (AcademicHub)** là nền tảng chia sẻ và đánh giá tài liệu học thuật theo mô hình cộng đồng đóng góp. Dự án được thiết kế theo kiến trúc **Modular Monolith** kết hợp **Asynchronous Background Worker**, tập trung giải quyết bài toán kiểm duyệt nội dung học thuật tự động bằng AI và gợi ý tài liệu cá nhân hóa dựa trên hành vi người dùng.

### 🌟 Điểm nổi bật kỹ thuật (Key Highlights)
* **Pipeline kiểm duyệt AI bất đồng bộ (AI Moderation Pipeline):** Tách biệt luồng upload tài liệu và xử lý AI qua **RabbitMQ**, đảm bảo throughput cao và trải nghiệm mượt mà cho người dùng.
* **Quyết định kiểm duyệt đa nhánh (Decision Engine):** Trích xuất nội dung bằng **Apache PDFBox**, phân tích qua **Gemini API** với **Structured Output** để phân loại thành 3 trạng thái: `APPROVED`, `NEEDS_REVIEW` (chuyển Admin duyệt thủ công kèm audit), hoặc `REJECTED`.
* **Chống trùng lặp & Lưu trữ an toàn:** Xác thực MIME/Magic bytes, tính toán mã băm **SHA-256** để ngăn chặn file trùng, lưu trữ Private trên **MinIO (S3-compatible)**.
* **Gợi ý theo ngữ cảnh (Recommendation Engine):** Tính toán **User Interest Profile** dựa trên ma trận trọng số tương tác (`VIEW`, `DOWNLOAD`, `BOOKMARK`, `RATING`) để đề xuất Top-N tài liệu phù hợp.

---

## 🏗️ 2. Kiến trúc hệ thống (System Architecture)

```
[ Client (React/Vite) ]
         │ (HTTP / REST API)
         ▼
[ Spring Boot REST API (Modular Monolith) ]
   ├── Auth & User Management (JWT, RBAC)
   ├── Document Management (Metadata, SHA-256)
   ├── Category & Tag Management
   ├── Recommendation Engine
   └── Storage Gateway
         │                                       │
    (Persist)                             (Publish Job)
         │                                       │
         ▼                                       ▼
  [ PostgreSQL 16 ]                      [ RabbitMQ Broker ]
  (Relational Data)                              │
                                           (Consume Job)
                                                 │
                                                 ▼
                                     [ AI Moderation Worker ]
                                     ├── PDFBox Text Extractor
                                     ├── Gemini AI Adapter
                                     ├── Decision Rule Engine
                                     └── MinIO Object Storage
```

---

## 🛠️ 3. Công nghệ sử dụng (Tech Stack)

| Lớp | Công nghệ | Mục đích |
| :--- | :--- | :--- |
| **Backend** | Java 25 LTS, Spring Boot 3.4.x | Nền tảng cốt lõi, RESTful API |
| **Frameworks** | Spring Data JPA, Spring Security, Validation | ORM, Xác thực & phân quyền, kiểm tra dữ liệu |
| **Database** | PostgreSQL 16, Flyway | Lưu trữ dữ liệu quan hệ, quản lý phiên bản migration |
| **Storage** | MinIO (S3 API) | Lưu trữ file PDF an toàn ở chế độ Private |
| **Queue** | RabbitMQ | Điều phối tác vụ kiểm duyệt AI bất đồng bộ |
| **AI Provider**| Google Gemini API | Phân loại nội dung, gán cờ rủi ro theo JSON Schema |
| **Frontend** | React, TypeScript, Vite, TailwindCSS | Giao diện người dùng & Admin dashboard |
| **DevOps** | Docker, Docker Compose | Đóng gói và chạy môi trường phát triển nhất quán |
| **Build Tool** | Gradle (Groovy DSL) | Quản lý phụ thuộc và quy trình build |

---

## 📂 4. Cấu trúc thư mục (Directory Structure)

```
EduShare/
├── backend/                  # Mã nguồn Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/edushare/
│   │   │   │   ├── common/       # DTO chuẩn, Exception Handling toàn cục
│   │   │   │   ├── category/     # Module danh mục tài liệu
│   │   │   │   ├── auth/         # Module xác thực JWT & Security
│   │   │   │   ├── document/     # Module upload & quản lý file
│   │   │   │   ├── moderation/   # Decision engine & pipeline AI
│   │   │   │   └── recommendation/# Thuật toán tính điểm gợi ý
│   │   │   └── resources/
│   │   │       ├── db/migration/ # Các script SQL của Flyway
│   │   │       └── application.yaml
│   ├── build.gradle          # Cấu hình Gradle
│   └── gradlew / gradlew.bat
├── frontend/                 # Giao diện người dùng (React + TS)
├── docs/                     # Tài liệu đặc tả BRS, SRS, Roadmap 8 tuần
├── docker-compose.yml        # Cấu hình dịch vụ hạ tầng (PostgreSQL, MinIO, RabbitMQ)
├── .gitignore
└── README.md
```

---

## 🚀 5. Hướng dẫn chạy cục bộ (Getting Started)

### Yêu cầu tiên quyết (Prerequisites)
* **Java:** JDK 25 LTS trở lên
* **Docker:** Docker Desktop đang chạy
* **Git**

### Các bước cài đặt & khởi chạy

1. **Khởi động dịch vụ cơ sở dữ liệu & hạ tầng:**
   ```bash
   # Tại thư mục gốc EduShare/
   docker compose up -d
   ```

2. **Chạy ứng dụng Backend:**
   ```bash
   cd backend
   
   # Trên Windows (PowerShell/CMD):
   .\gradlew.bat bootRun

   # Trên Linux/macOS:
   ./gradlew bootRun
   ```

3. **Kiểm tra trạng thái:**
   * Backend API: `http://localhost:8080`
   * Database: Cổng `5432` (Database: `edushare`, User: `postgres`)

---

## 📅 6. Lộ trình phát triển 8 tuần (8-Week Roadmap)

* [x] **Week 1:** Setup nền tảng, Docker Compose, PostgreSQL, Flyway, Category CRUD, Global Exception Handler.
* [ ] **Week 2:** Authentication & Spring Security (JWT, RBAC USER/ADMIN).
* [ ] **Week 3:** Document Management (Upload PDF, SHA-256 deduplication, Private MinIO Storage).
* [ ] **Week 4:** Frontend Integration (React + TypeScript + Vite, Auth & Document UI).
* [ ] **Week 5:** AI Moderation Pipeline (PDFBox text extraction, Gemini Structured Output, Decision Engine).
* [ ] **Week 6:** Asynchronous Queue & Resilience (RabbitMQ worker, retry backoff, DLQ, 429 rate-limit handling).
* [ ] **Week 7:** Interaction Tracking & Rule-based Recommendation Engine.
* [ ] **Week 8:** End-to-end Integration Testing, Full-stack Docker Compose, Demo & Portfolio Polish.

---

## 📄 License
Dự án được phát triển cho mục đích học tập và xây dựng portfolio cá nhân.
