🗞️ Local News Experience — Full-Stack App
This project delivers local news for all cities in the United States.
Users can search for their city and view local news, along with a selection of global news.
The application is built with React (frontend) and Java (backend).
Data is processed by a Python pipeline using OpenAI GPT-4o-mini.

---

📦 Backend (Java)
Spring Boot application exposing a REST API:

/api/cities — returns a list of cities (from a built-in JSON file)

/api/news?city=CityName — returns news assigned to the selected city

/api/news/articleId — returns the full content of a selected article

The list of U.S. cities is loaded from a JSON file at startup and kept in memory.

💻 Frontend (React)
A simple interface for searching cities and displaying news articles.

---

🗄️ Data
🏙️ U.S. Cities
Source: GitHub

JSON file loaded into the backend at runtime

📰 News Articles
Scraped using Python scripts from https://patch.com

~150 articles:

~80% local (assigned to specific cities)

~20% global

---

🧠 Data Pipeline (Python)
The articles_transformer.py script uses the OpenaiGuesser class powered by gpt-4o-mini.

It performs the following:
  * Determines whether an article is local or global
  * If local — assigns the article to a U.S. city
  * Saves the results to a JSON file used by the backend

---

🧪 Running the Project
* With Dockerfile