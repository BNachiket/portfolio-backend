# Portfolio API (Spring Boot + MongoDB)

Backend for the React portfolio. Java 17, Spring Boot 3.3, Spring Data MongoDB.
All portfolio content lives in MongoDB and is read from there on every request.

## Endpoints

| Method | Path                | Reads from collection |
|--------|---------------------|-----------------------|
| GET    | /api/health         | (none, health check)  |
| GET    | /api/portfolio      | all of the below      |
| GET    | /api/profile        | `profile`             |
| GET    | /api/skills         | `skills`              |
| GET    | /api/experience     | `experience`          |
| GET    | /api/projects       | `projects`            |
| GET    | /api/education      | `education`           |
| GET    | /api/certifications | `certifications`      |
| GET    | /api/publications   | `publications`        |
| POST   | /api/contact        | writes to `contact_messages` (JSON `{name, email, message}`) |

## MongoDB collections

| Collection        | Documents                       | Main fields                                   |
|-------------------|---------------------------------|-----------------------------------------------|
| `profile`         | 1 (`_id: "main"`)               | name, title, email, phone, linkedin, photo, summary |
| `skills`          | one per category                | category, items[], position                   |
| `experience`      | one per job                     | role, company, period, bullets[], position    |
| `projects`        | one per project                 | name, subtitle, tags[], bullets[], position   |
| `education`       | one per qualification           | degree, institution, year, score, position    |
| `certifications`  | one per certification           | title, position                               |
| `publications`    | one per publication             | citation, position                            |
| `contact_messages`| one per form submission         | name, email, message, createdAt               |

`position` controls display order (0 is first).

### How the data gets into MongoDB

On startup `DataSeeder` copies `src/main/resources/portfolio-seed.json` into the collections above,
but only for collections that are empty. After the first start, MongoDB is the source of truth:

- Edit, add or delete documents in Atlas or Compass and the website shows the change on the next load.
- Changing `portfolio-seed.json` later does **not** change existing data.
- To reload everything from the JSON file, set `SEED_RESET=true` for one start (this wipes the
  portfolio collections, not `contact_messages`), then remove the variable.
- Set `SEED_ENABLED=false` to turn seeding off completely.

If you ran the earlier version of this project, a `portfolio` collection (single document) may exist
in your database. It is no longer used and can be dropped.

## Configuration (environment variables)

| Variable       | Default                                       | Purpose                                   |
|----------------|-----------------------------------------------|-------------------------------------------|
| `MONGODB_URI`  | `mongodb://localhost:27017/portfolio`         | MongoDB connection string                 |
| `CORS_ORIGINS` | `http://localhost:5173,http://localhost:3000` | Allowed frontend origins, comma separated |
| `PORT`         | `8080`                                        | HTTP port (hosts set this automatically)  |
| `SEED_ENABLED` | `true`                                        | Seed empty collections on startup         |
| `SEED_RESET`   | `false`                                       | Wipe and reseed portfolio collections     |

## Run locally

Option A, MongoDB in Docker (the compose file is in the project root):

```bash
docker compose -f ../docker-compose.yml up -d
mvn spring-boot:run
```

Option B, MongoDB Atlas:

```powershell
# Windows PowerShell
$env:MONGODB_URI="mongodb+srv://USER:PASSWORD@cluster.mongodb.net/portfolio"
mvn spring-boot:run
```

```bash
# macOS/Linux
export MONGODB_URI="mongodb+srv://USER:PASSWORD@cluster.mongodb.net/portfolio"
mvn spring-boot:run
```

On first start the log shows `Seeded collection 'skills' with 6 document(s)` and similar lines.
Then open http://localhost:8080/api/portfolio.

Run the tests with `mvn test` (they do not need MongoDB).

## Deploy

1. **Database:** create a free cluster on MongoDB Atlas. Add a database user, and under Network
   Access allow `0.0.0.0/0` (hosts like Render use changing IPs). Copy the connection string and
   include the database name, e.g. `.../portfolio?retryWrites=true&w=majority`.
2. **Backend (Render):** New → Web Service → connect the GitHub repo → Runtime: Docker. Add
   `MONGODB_URI` and `CORS_ORIGINS` (your Vercel URL, no trailing slash). Health check path: `/api/health`.
3. **Frontend (Vercel):** set `VITE_API_URL` to the Render URL and redeploy.

Special characters in the Atlas password must be URL-encoded (for example `@` becomes `%40`).
