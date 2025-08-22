# Financial Portfolio Tracker

The **Financial Portfolio Tracker** is a Spring Boot application designed to manage and track financial transactions, including assets, transaction types, and their associated prices. It integrates with the Twelve Data API to fetch historical price data for assets.

## Features

- **Transaction Management**: Create, retrieve, and manage financial transactions.
- **External API Integration**: Fetch historical asset prices using the Twelve Data API.
- **Database Integration**: Store and retrieve transaction data using a repository layer.

## Technologies Used

- **Java**: Core programming language.
- **Spring Boot**: Framework for building the application.
- **RestTemplate**: For making HTTP requests to external APIs.
- **Twelve Data API**: For fetching historical price data.
- **Dotenv**: For managing environment variables.
- **JSON**: For parsing API responses.

## Prerequisites

- **Java 17+**
- **Maven**: For building the project.
- **Twelve Data API Key**: Obtain an API key from [Twelve Data](https://twelvedata.com/) and store it in a `.env` file.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/RafalPy/financial-portfolio-tracker.git
   cd financial-portfolio-tracker
   ```

2. **Create a `.env` File**:
   Create a `.env` file in the root directory and add your Twelve Data API key:
   ```
   TWELVE_API_KEY=your_api_key_here
   ```

3. **Build the Project**:
   Use Maven to build the project:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
   The application will run on `http://localhost:8080`.

## API Endpoints

- **Create Transaction**: `POST /transactions`
- **Get All Transactions**: `GET /transactions`

## Example `.env` File

```plaintext
TWELVE_API_KEY=your_api_key_here
```

## Project Structure

- **`TransactionService`**: Handles business logic for transactions, including fetching prices from the Twelve Data API.
- **`TransactionRepository`**: Interface for database operations.
- **Entities**: Represent the data model (e.g., `Transaction`, `AssetType`, `TransactionType`).

## Error Handling

If the price for a given asset symbol and date cannot be found, the application will throw a `RuntimeException` with a descriptive error message.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- [Twelve Data API](https://twelvedata.com/) for providing financial data.
- Spring Boot for simplifying application development.