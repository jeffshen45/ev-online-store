# EV Online Store
EV e-commerce platform developed for EECS 4413.

## How to Run the Application
1. Clone or download the repository.
2. In Eclipse (or preferred IDE), right-click the project folder -> **Maven** -> **Update Project...**
3. Navigate to `src/main/java` -> `com.evstore`.
4. Right-click `EvstoreApplication.java` -> **Run As** -> **Java Application**.
5. Open your web browser and navigate to `http://localhost:8080`.

## Web UI & User Navigation
The application provides browser accessible Thymeleaf views connecting directly to the service layer and database persistence:
* **Landing Page :** Central navigation hub linking to vehicle catalog, account management, and test drive booking.
* **Customer Registration (`/register`):** Form interface for creating customer accounts (`username`, `email`, `password`, `address`).
* **Customer Sign-In (`/login`):** Session-based authentication verifying customer credentials (`email`, `password`).
* **Book Test Drive (`/testdrive`):** Scheduling interface for test drive reservations (dealership or home delivery) with dynamic validation restricting selection to current/future dates.

## API Endpoints (Account & Test Drive)
* `POST /api/auth/register` - Create customer account
* `POST /api/auth/login` - Authenticate customer and establish session
* `POST /api/auth/logout` - Invalidate active customer session
* `GET /api/auth/status` - Check current session status
* `POST /api/testdrive/book` - Reserve a test drive slot
* `GET /api/testdrive/customer/{id}` - Fetch customer test drive bookings

## Running Tests
Run the automated test suite directly through Maven or the IDE JUnit test runner:
```bash
./mvnw clean test
```




