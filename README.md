# Flowcharts and Sequence Diagrams
## App Sequence diagram
```mermaid
sequenceDiagram 
    participant Player
    participant Frontend
    participant API
    participant Backend
    
    Player ->> Frontend: Player presses start and takes a picture
    Frontend ->> API: Request to identify all objects in picture
    API ->> Backend: Backend recieves picture and performs object recognition
    Backend ->> API: Backend sends objects to API to return to caller
    API ->> Frontend: API passes arraylist of objects frontend
    Frontend ->> Player: Prompt to take another photo
    
    Player ->> Frontend: Takes another picture
    Frontend ->> API: Request to identify all objects in picture
    API ->> Backend: Backend recieves picture and performs object recognition
    Backend ->> API: Backend sends objects to API to return to caller
    API ->> Frontend: API passes arraylist of objects frontend
    Frontend ->> Player: Processes multiple choice quiz and displays options to user 
    
    Player ->> Frontend: Selected Option
    Frontend ->> Player: Returns Result to user and prompts play again
```
```mermaid
sequenceDiagram
    participant Main
    participant PictureCapture
    participant ObjectDetection
    participant Turnaround
    participant GameMode
    
    Main ->> PictureCapture: User selects which gameMode
    PictureCapture ->> ObjectDetection: First picture is processed for object detection
    ObjectDetection ->> PictureCapture: Data from first picture returned in a list
    PictureCapture ->> Turnaround: List of detected objects is stored
    Turnaround ->> PictureCapture: Second picture needs to be taken
    PictureCapture ->> ObjectDetection: Second picture is processed for object detection
    ObjectDetection ->> PictureCapture: Data from second picture returned in a list
    PictureCapture ->> GameMode: Game mode is loaded and played using both lists to compare for the missing item
```
## Frontend
```mermaid
flowchart LR
    Camera((Camera)) -->|Takes photo| Manager
    Manager -->|Request items from API| RequestList -->|Recieved items list from API| mod2{Is photo count % 2 = 0}
    mod2 .->|Yes| ListsCMP[Compare lists of items]
    mod2 .->|No| ListsStore[Store list of items from API]
    ListsStore -.->|Wait for next photo to be taken| Camera
    ListsCMP -->|Select what is missing and 3 random items| Options[Get game options]
    Options -->|User selects wrong answer| WrongAnswer[Wrong]
    WrongAnswer -->|Show the wrong answer UI| ShowWrong[Show wrong answer UI]
    Options -->|User selects correct answer| RightAnswer[Correct]
    RightAnswer -->|Show the Right answer UI| ShowRight[Show right answer UI]
    ShowWrong & ShowRight --> Again?
    Again? .->|Yes| Camera
    Again? .->|No| ThanksForPlaying(((Display thanks for playing)))
    style Camera stroke:#f66,stroke-width:2px,stroke-dasharray: 10 5
    style ThanksForPlaying stroke:#f66,stroke-width:2px,stroke-dasharray: 10 5
```
## API

#### The API
The API is hosted on aws and utilises AWS serverless technology.
To access the API we use an AWS API Gateway with an endpoint `getItemsFromImage` set to open.
Setting this endpoint to open means that anyone is able to access the endpoint without setting up a vpc.
This endpoint is also set up as regional to use the region which is closest to the UK for lowest latency.

#### The Lambda

This endpoint calls an AWS Lambda to run what it is needed to run.
An AWS Lambda is the serverless technology previously talked about. Imagine an AWS Lambda as a server which
only runs when it is needed to run. This results in a very cheap alternative to running a server.
The AWS Lambda that we are running uses python. This python Lambda calls the method Lambda Handler. 
This is where the code will start to be run. It is passed in an event and the context of the event.
Depending on the action you can return from the lambda a body and a status code or just a status code.
The body is a json string of data often times seen as the result. The status code is an identifier which can
result in a quick classification of the content, if the status code is 200 it was a success,
if the status code was a 401 it was unauthorized, etc.

#### Status Code Cheatsheet

For more info on the status codes which will be used, use this. https://restfulapi.net/http-status-codes/

### State Diagram

```mermaid
stateDiagram-v2 
    idleState: This is the idle state
    requestAPI: The API contacts the backend for the arraylist
    returnBackend: Backend returns the list of items
    returnToCaller: Caller has araylist of items
    
    idleState --> idleState: Waiting for request
    idleState --> requestAPI: The API is requested for the arraylist
    requestAPI --> requestAPI: Waiting for the backend
    requestAPI --> returnBackend: API has requested arraylist
    returnBackend --> returnToCaller: API returns the list of items to the caller
    returnToCaller --> idleState: Prepared for new API call
```
## Backend
```mermaid
flowchart LR
    APICall((API)) 
        -->|Request objects from picture| ObjCall[[Recieve objects\nrequest from API]]
        -->|Prepare image for AI| PreAI[Prepare for AI]
        -->|Request objects from AI| AIRequest[Request objects list from AI]
        .->|Revieve objects from AI| AIRecieve[Recieved objects list from AI]
        -->|Gives API the list to send to the frontend| APISend((Send to API))
        
    style APICall stroke:#f66,stroke-width:2px,stroke-dasharray: 10 5
    style APISend stroke:#f66,stroke-width:2px,stroke-dasharray: 10 5
```
## AI
```mermaid
stateDiagram-v2
    idleStart: This is the idle state
    requestAPI: The AI requests the image from the API
    analyseImage: The AI analyses the image
    produceList: The AI produces an arrayList
    returnList: The AI returns the arrayList to the API
    
    idleStart --> idleStart: Waiting for request
    idleStart --> requestAPI: Image is requested from the API
    requestAPI --> analyseImage
    analyseImage --> produceList
    produceList --> returnList

    returnList --> idleStart: Prepared for new request
```

```mermaid
sequenceDiagram
    User->>+Frontend: User takes a photo
    Frontend->>+Backend: Request image processing
    Backend->>+API: Send base 64 encoded image to be processed
    API-->>+AI: Detect objects and blur one with highest confidence
    AI-->>-API: Return blurred image with the name of the object blurred
    API-->>Backend: Send image with answer to check against the user's answer
    Backend-->>Frontend: Enter game mode activity
    Frontend-->>User: Prompt user to enter answer
    User-->>Frontend: User enters answer
    Frontend-->>-Backend: Check user's answer against stored answer
    Backend-->>-Frontend: Display result
```