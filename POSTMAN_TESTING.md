# SMS Backend — Postman Testing Sheet

> **Base URL:** `http://localhost:8080`  
> **Start the server first:** `./mvnw spring-boot:run`  
> Save the JWT token from Step 2 — you need it for everything else.

---

## 🟢 STEP 1 — Health Check (No Auth)

| # | Method | URL | Expected |
|---|--------|-----|----------|
| 1.1 | GET | `/check` | `200 OK` — body: `OK!` |

---

## 🔐 STEP 2 — Authentication

### 2.1 Login — Valid Credentials
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin@123"
}
```
✅ **Expected:** `200 OK`
```json
{
  "token": "eyJ...",
  "expiresIn": 86400000
}
```
> **Save the token.** Add it to all future requests as:  
> `Authorization: Bearer <token>`

---

### 2.2 Login — Wrong Password
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "wrongpassword"
}
```
✅ **Expected:** `401 Unauthorized`
```json
{
  "timestamp": "...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

---

### 2.3 Hit Protected Route Without Token
```
GET /api/owners
(No Authorization header)
```
✅ **Expected:** `401 Unauthorized` or `403 Forbidden`

---

## 👤 STEP 3 — Owner CRUD

> Add header: `Authorization: Bearer <token>` to all requests below.

### 3.1 Create Owner — Valid
```
POST /api/owners
Content-Type: application/json

{
  "name": "Ramesh Patil",
  "nameInMarathi": "रमेश पाटील",
  "flatNumber": "A-101",
  "phoneNo": "9876543210"
}
```
✅ **Expected:** `201 Created`
```json
{
  "id": 1,
  "name": "Ramesh Patil",
  "nameInMarathi": "रमेश पाटील",
  "flatNumber": "A-101",
  "phoneNo": "9876543210"
}
```
> **Save `id` (e.g. 1) — used in next steps.**

---

### 3.2 Create Owner — Missing Name (Validation)
```
POST /api/owners
Content-Type: application/json

{
  "name": "",
  "flatNumber": "B-202",
  "phoneNo": "9123456780"
}
```
✅ **Expected:** `400 Bad Request`
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Owner name must not be blank"
}
```

---

### 3.3 Create Owner — Invalid Phone Number
```
POST /api/owners
Content-Type: application/json

{
  "name": "Test User",
  "flatNumber": "C-303",
  "phoneNo": "12345"
}
```
✅ **Expected:** `400 Bad Request` — phone validation error

---

### 3.4 Create Second Owner
```
POST /api/owners
Content-Type: application/json

{
  "name": "Sunita Sharma",
  "flatNumber": "B-202",
  "phoneNo": "9123456780"
}
```
✅ **Expected:** `201 Created` — save this `id` too (e.g. 2)

---

### 3.5 Get All Owners
```
GET /api/owners
```
✅ **Expected:** `200 OK` — array with both owners

---

### 3.6 Get Owner By ID
```
GET /api/owners/1
```
✅ **Expected:** `200 OK` — Ramesh Patil

---

### 3.7 Get Owner By Flat Number
```
GET /api/owners/flat/A-101
```
✅ **Expected:** `200 OK` — Ramesh Patil

---

### 3.8 Get Owner — Not Found
```
GET /api/owners/9999
```
✅ **Expected:** `404 Not Found`
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Owner not found with id: 9999"
}
```

---

### 3.9 Update Owner
```
PUT /api/owners/1
Content-Type: application/json

{
  "name": "Ramesh Patil Updated",
  "nameInMarathi": "रमेश पाटील",
  "flatNumber": "A-101",
  "phoneNo": "9876543210"
}
```
✅ **Expected:** `200 OK` — updated name in response

---

### 3.10 Delete Owner (use id=2 — Sunita)
```
DELETE /api/owners/2
```
✅ **Expected:** `204 No Content`

Confirm deletion:
```
GET /api/owners/2
```
✅ **Expected:** `404 Not Found`

---

## 🔧 STEP 4 — Maintenance

### 4.1 Create Maintenance Record (for Owner 1)
```
POST /api/maintenance
Content-Type: application/json

{
  "ownerId": 1,
  "fromDate": "2026-04-01",
  "toDate": "2026-04-30",
  "amount": 2500.00
}
```
✅ **Expected:** `201 Created`
```json
{
  "id": 1,
  "ownerId": 1,
  "ownerName": "Ramesh Patil Updated",
  "flatNumber": "A-101",
  "fromDate": "2026-04-01",
  "toDate": "2026-04-30",
  "amount": 2500.00,
  "status": "PENDING",
  "paymentDate": null
}
```
> **Save maintenance `id` (e.g. 1)**

---

### 4.2 Create Another Maintenance Record
```
POST /api/maintenance
Content-Type: application/json

{
  "ownerId": 1,
  "fromDate": "2026-05-01",
  "toDate": "2026-05-31",
  "amount": 2500.00
}
```
✅ **Expected:** `201 Created` — id=2

---

### 4.3 Get Maintenance — Invalid Amount
```
POST /api/maintenance
Content-Type: application/json

{
  "ownerId": 1,
  "fromDate": "2026-06-01",
  "toDate": "2026-06-30",
  "amount": 0
}
```
✅ **Expected:** `400 Bad Request` — amount validation error

---

### 4.4 Get All Pending
```
GET /api/maintenance/pending
```
✅ **Expected:** `200 OK` — both records with `"status": "PENDING"`

---

### 4.5 Get Maintenance By Owner
```
GET /api/maintenance/owner/1
```
✅ **Expected:** `200 OK` — list of maintenance for Owner 1

---

### 4.6 Get Single Maintenance
```
GET /api/maintenance/1
```
✅ **Expected:** `200 OK`

---

### 4.7 Mark Maintenance as PAID ⭐ (key business flow)
```
PATCH /api/maintenance/1/pay
```
✅ **Expected:** `200 OK`
```json
{
  "id": 1,
  "status": "PAID",
  "paymentDate": "2026-05-24"
}
```

---

### 4.8 Try to Pay Again — Should Conflict
```
PATCH /api/maintenance/1/pay
```
✅ **Expected:** `409 Conflict`
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Maintenance id=1 is already marked as PAID"
}
```

---

### 4.9 Check Pending — Should Only Show Record 2 Now
```
GET /api/maintenance/pending
```
✅ **Expected:** `200 OK` — only 1 record (id=2, still PENDING)

---

## 🧾 STEP 5 — Receipts

> Receipt for maintenance id=1 was auto-generated when you called `/pay`

### 5.1 Get Receipt By Maintenance ID
```
GET /api/receipts/maintenance/1
```
✅ **Expected:** `200 OK`
```json
{
  "id": 1,
  "receiptNumber": "REC-202605-0001",
  "generatedAt": "2026-05-24T...",
  "ownerName": "Ramesh Patil Updated",
  "flatNumber": "A-101",
  "fromDate": "2026-04-01",
  "toDate": "2026-04-30",
  "amount": 2500.00
}
```
> **Save receipt `id` (e.g. 1)**

---

### 5.2 Get Receipt By Receipt ID
```
GET /api/receipts/1
```
✅ **Expected:** `200 OK` — same data as above

---

### 5.3 Get Receipt For PENDING Maintenance — Should Fail
```
GET /api/receipts/maintenance/2
```
✅ **Expected:** `404 Not Found` — no receipt for unpaid maintenance

---

### 5.4 Download Receipt PDF ⭐
```
GET /api/receipts/1/download
```
✅ **Expected:**
- `200 OK`
- `Content-Type: application/pdf`
- `Content-Disposition: attachment; filename="receipt-REC-202605-0001.pdf"`
- Binary PDF body (Postman shows "Save response to file" option)

> In Postman → click **Send and Download** to save and open the PDF file

---

## 💰 STEP 6 — Expenses

### 6.1 Create Expense
```
POST /api/expenses
Content-Type: application/json

{
  "title": "Water Tank Cleaning",
  "category": "Maintenance",
  "amount": 3500.00,
  "date": "2026-05-10",
  "paidTo": "Suresh Plumbers",
  "note": "Annual tank cleaning"
}
```
✅ **Expected:** `201 Created`
```json
{
  "id": 1,
  "title": "Water Tank Cleaning",
  "category": "Maintenance",
  "amount": 3500.00,
  "date": "2026-05-10",
  "paidTo": "Suresh Plumbers",
  "note": "Annual tank cleaning"
}
```

---

### 6.2 Create Another Expense
```
POST /api/expenses
Content-Type: application/json

{
  "title": "Electricity Bill",
  "category": "Utility",
  "amount": 8200.00,
  "date": "2026-05-15",
  "paidTo": "MSEB",
  "note": "Common area electricity"
}
```
✅ **Expected:** `201 Created`

---

### 6.3 Create Expense — Missing Required Field
```
POST /api/expenses
Content-Type: application/json

{
  "title": "",
  "category": "Test",
  "amount": 100.00,
  "date": "2026-05-20",
  "paidTo": "Someone"
}
```
✅ **Expected:** `400 Bad Request` — title validation error

---

### 6.4 Get All Expenses
```
GET /api/expenses
```
✅ **Expected:** `200 OK` — array of 2 expenses

---

### 6.5 Get Expenses By Date Range
```
GET /api/expenses/range?from=2026-05-01&to=2026-05-31
```
✅ **Expected:** `200 OK` — both expenses (both in May 2026)

---

### 6.6 Get Expenses — Empty Range
```
GET /api/expenses/range?from=2026-01-01&to=2026-01-31
```
✅ **Expected:** `200 OK` — empty array `[]`

---

### 6.7 Delete Expense
```
DELETE /api/expenses/2
```
✅ **Expected:** `204 No Content`

Confirm:
```
GET /api/expenses
```
✅ **Expected:** Only 1 expense remains

---

## 📊 STEP 7 — Dashboard

### 7.1 Get Dashboard Summary
```
GET /api/dashboard
```
✅ **Expected:** `200 OK`
```json
{
  "totalOwners": 1,
  "totalCollected": 2500.00,
  "totalPending": 2500.00,
  "totalExpenses": 3500.00,
  "currentMonthCollection": 2500.00,
  "currentMonthExpenses": 3500.00
}
```
> Numbers reflect exactly what you've created in steps 3–6.

---

## 🛡️ STEP 8 — Security Edge Cases

### 8.1 Expired / Garbage Token
```
GET /api/owners
Authorization: Bearer this.is.not.a.valid.token
```
✅ **Expected:** `403 Forbidden` or `401 Unauthorized`

---

### 8.2 No Token on Protected Route
```
POST /api/expenses
Content-Type: application/json
(No auth header)

{ "title": "Test", "category": "X", "amount": 100, "date": "2026-05-01", "paidTo": "Y" }
```
✅ **Expected:** `401` or `403` — not `201`

---

### 8.3 Login Endpoint Is Public (No Token Needed)
```
POST /api/auth/login
(No auth header)

{ "username": "admin", "password": "Admin@123" }
```
✅ **Expected:** `200 OK` — login works without token

---

## ✅ Phase Completion Checklist

| Phase | What to Verify | Status |
|-------|---------------|--------|
| Phase 1 | Mappers compile, `status`/`paymentDate` appear in maintenance response | ☐ |
| Phase 2 | Service logic works, `markAsPaid` triggers receipt | ☐ |
| Phase 3 | All endpoints respond with correct HTTP codes | ☐ |
| Phase 4 | Error responses have `timestamp`, `status`, `error`, `message`, `path` | ☐ |
| Phase 5 | Login returns JWT, protected routes return 401 without token | ☐ |
| Phase 6 | PDF downloads, opens correctly, shows all receipt details | ☐ |
| Phase 7 | Blank/null fields return 400 with validation messages | ☐ |
| Phase 8 | Dashboard numbers match what you created | ☐ |

---

## 🔑 Postman Environment Setup (Recommended)

Create a Postman **Environment** with these variables:

| Variable | Initial Value |
|----------|--------------|
| `base_url` | `http://localhost:8080` |
| `token` | *(paste after login)* |

Then use `{{base_url}}/api/owners` and `Authorization: Bearer {{token}}` across all requests.
