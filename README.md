# 🤖 Mind-Code Assistant 

<p align="center">
<img src="screenshots/app-preview.png" alt="Mind-Code Assistant Preview" width="1000">
</p>

An intelligent, empathetic coding companion built with Spring Boot and Vanilla JavaScript. 
It is designed to be a flexible development partner, offering a helping hand during technical challenges as well as being your supportive anchor when imposter syndrome kicks in.
By focusing on both clean code and emotional support, it ensures you never have to face a complex bug or a moment of self-doubt alone.

---

## ✨ Features
* **Personality Modes**: Toggle between **Code-Helper**, **Assistant**, and the **Mood-Booster**—a dedicated system prompt to provide encouragement and perspective when coding stress kicks in.
* **Markdown Support**: Full rendering of code blocks, bold text, and lists for clear technical communication.
* **Flexible Backend**: Support for any OpenAI-compatible API.
* **Resilient Architecture**: Built with **Resilience4j** (Circuit Breaker and Retries) to ensure a stable experience even during service interruptions.


## 🛠️ Tech Stack
* **Backend –** Java 25 & Spring Boot 4.0.6
* **Frontend –** Thymeleaf, Vanilla JavaScript, Modern CSS
* **Stability**: Resilience4j (Circuit Breaker & Retry).
* **Markdown**: Marked.js for real-time rendering.

---

## 🚀 How to Run

To use this assistant, you need an AI backend. You can choose to run it **locally** (free) or via the **cloud** (OpenRouter).

### 1. Setup your AI Backend (Choose one)

#### **Option A: Local (LM Studio)**
1.  **Download:** Install [LM Studio](https://lmstudio.ai/).
2.  **Model:** Search for and download a model (e.g., `Llama 3.1 8B`).
3.  **Server:** Go to the **Local Server** tab, set port to `1234`, and click **Start Server**.

#### **Option B: Cloud (OpenRouter)**
1.  **API Key:** Get a key from [OpenRouter](https://openrouter.ai/).
2.  **Environment Variables:** In your IDE (IntelliJ), go to **Run > Edit Configurations** and add:
    * `AI_API_BASE_URL` = `https://openrouter.ai/api/v1`
    * `AI_API_KEY` = `your_key_here`
    * `AI_API_MODEL_NAME` = `meta-llama/llama-3.1-8b-instruct`

### 2. Launch the Application
1.  **Clone:** Clone the repository and open it in your IDE.
2.  **Run:** Execute `AiIntegratedChatbotApplication.java`.
3.  **Access:** Open your browser and go to: `http://localhost:8080`

---

## 📖 API Documentation

The project includes an interactive Swagger UI, allowing you to explore and test the backend endpoints (like `/ai/chat`) easily.

* **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **OpenAPI Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ⚙️ Configuration Details

The application uses **Environment Variables** to manage settings. If no variables are set, it defaults to the local LM Studio setup.

| Variable | Description | Default (Local Fallback) |
| :--- | :--- | :--- |
| `AI_API_BASE_URL` | The API endpoint | `http://localhost:1234/v1` |
| `AI_API_MODEL_NAME` | The model identifier | `meta-llama-3.1-8b-instruct` |
| `AI_API_KEY` | Secret API key | `no-key-needed` |
