# 🧪 Local CI & Testing Commands

## 1. Quick Check Trước Khi Push (Khuyên dùng)
```powershell
# Chạy tại backend/
.\gradlew spotlessCheck test jacocoTestReport
```

```powershell
# Hoặc chạy từ thư mục gốc EduShare/
./backend/gradlew -p backend spotlessCheck test jacocoTestReport
```

---

## 2. Backend Commands (Chạy tại `backend/`)

### Code Style & Format
```powershell
# Kiểm tra format
.\gradlew spotlessCheck

# Tự động sửa format
.\gradlew spotlessApply
```

### Tests & Coverage
```powershell
# Chạy test
.\gradlew test

# Ép chạy lại toàn bộ test (không dùng cache)
.\gradlew test --rerun

# Xuất báo cáo Jacoco Coverage
.\gradlew jacocoTestReport
```

### Build & Package
```powershell
# Build hoàn chỉnh (format + test + jar)
.\gradlew build

# Build bỏ qua test
.\gradlew build -x test

# Dọn dẹp thư mục build
.\gradlew clean

# Dừng Gradle Daemon khi bị đơ/lock
.\gradlew --stop
```

---

## 3. Đường dẫn xem Báo cáo HTML (Mở bằng trình duyệt)
- **Test Results:** `backend/build/reports/tests/test/index.html`
- **JaCoCo Coverage:** `backend/build/reports/jacoco/test/html/index.html`

---

## 4. Frontend Commands (Chạy tại `frontend/`)
```powershell
# Kiểm tra lint
npm run lint

# Build production & kiểm tra type
npm run build
```

---

## 5. Docker Compose Commands (Chạy tại thư mục gốc)
```powershell
# Build và chạy ngầm toàn bộ service
docker compose up -d --build

# Xem log backend
docker compose logs -f backend

# Tắt toàn bộ service
docker compose down
```
