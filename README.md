# TikTok Telegram Bot

---

## 🧩 Project Description
**TikTok Telegram Bot** this is a bot that allows users to **download TikTok videos without watermark**.To access the download functionality, users must **subscribe to sponsor Telegram channels**,
enabling a monetization model via forced subscription.

---

## 🚀 Features
- Download TikTok videos without watermark
- Requires subscription to sponsor channels before allowing download
- Checker whether the user has subscribed to the required channels
- Admin commands for managing sponsors and users
- Telegram Bot interface
- Monetization through sponsor subscriptions
- All users receive an advert in the chatbot that will be sent by the admin

---

## 🔐 Admin Features

Admins can access privileged commands, such as:

- `/administrator add [channelId] [channelLink]` – Add a sponsor channel
- `/administrator delete [channelId]` – Remove a sponsor channel
- `/administrator channels` – View current sponsor channels
- `/administrator users` – View current active/inactive users count
- `/administrator ban [userId]` – Ban a user by id
- `/administrator unban [userId]` – Unban a user by id

---

## 📢 Advertising & Logs & Subscriptions Features
This bot includes advanced messaging features for advertising and logging via dedicated Telegram channels.

### 📢 Advertising
- Admins can post messages in a separate Ads Channel.
- Every new post in this channel will be automatically forwarded to all bot users.
- This allows promoting products, services, or sponsor content directly to the audience

    > ✅ Make sure to set the BOT_ADS_CHAT_ID in your environment variables.

### 📄 Logs
- The bot sends internal logs (errors, actions, user commands) to a dedicated Log Channel.
- Useful for monitoring bot activity, debugging, and maintaining transparency.
- Includes user actions, failed downloads, banned users, etc.

    >✅ Make sure to set the BOT_LOG_CHAT_ID in your environment variables.

### 🔔 Subscription Enforcement

- The bot **enforces subscription** to specific **sponsor Telegram channels** before allowing video downloads.
- If a user is not subscribed, the bot will prompt them to join and provide a "Check" button to verify.
- The subscription check is performed via Telegram’s channel membership status.
> ⚠️ **Important:** If the bot is removed from a sponsor channel (or banned), it will **automatically remove** that channel from the list of required subscriptions to prevent errors and dead checks.

> 🔐 **The bot must be an admin in each sponsor channel** to check user membership.

## 🧰 Tech stack used in this project
- **Java 21**
- **Spring Boot**
- **Hibernate**
- **PostgreSQL** (Database)
- **Liquibase** (Database migrations)
- **Lombok** (for reducing boilerplate code)
- **Maven** (Build tool)
- **Docker** (Containerization)
- **JUnit & Mockito** (Testing)

---

## ⚙️ Installation and Setup

**1. Clone the Repository**

```
git clone https://github.com/BohdanMoldavets/tiktok-telegram-bot.git
cd tiktok-telegram-bot
```

**2. Create the environment file and set up this variables**

```
touch variables.env 
```
These variables are required!
```
BOT_ADMIN_ID={your_id}
BOT_NAME={your_bot_name}
BOT_TOKEN={your_bot_token}
BOT_ADS_CHAT_ID={your_ads_chat_id}
BOT_LOG_CHAT_ID={your_log_chat_id}
```

**3. Add the bot to Ads & Logs channels**

**4. Run the application with Docker**
```
docker-compose up --build
```

# Contact
+ Email: [steamdlmb@gmail.com](mailto:steamdlmb@gmail.com)
+ [Telegram](https://telegram.me/moldavets)
+ [Linkedin](https://www.linkedin.com/in/bohdan-moldavets/)
