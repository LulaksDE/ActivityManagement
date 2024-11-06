# ActivityManagement


### TODO:

- [X] Initiale Dateien anlegen
- [X] Labels für relevante Felder erstellen
- [X] Bearbeitung von Aktivitäten implementieren
- [ ] Historie für erstellte/beendete Aktivitäten
- [X] Projekt Filter
- [ ] Filter Aktivitäten
- [ ] Suche für Aktivitäten implementieren
- [X] Priorität
- [ ] Status (inkl. Status Labels?)
- [ ] Kanban-Board
- [ ] Drag and drop in Kanban-Board
- [ ] Kategorisierung der Projekte
- [ ] Kommentare im Nachgang
- [ ] Historie
- [ ] Dark-Mode
- [ ] Abbrechen Projekterstellung
Test

## Projekt UML

``` mermaid

classDiagram
    class Activity {
        -String id
        -String title
        -String description
        -LocalDate dueDate
        -boolean completed
        +getId(): String
        +getTitle(): String
        +setTitle(String): void
        +getDescription(): String
        +setDescription(String): void
        +getDueDate(): LocalDate
        +setDueDate(LocalDate): void
        +isCompleted(): boolean
        +setCompleted(boolean): void
    }

    class Project {
        -String name
        -List~Activity~ activityList
        +addActivity(Activity): void
        +removeActivity(Activity): void
        +getActivityList(): List~Activity~
        +getName(): String
        +setName(String): void
    }

    class User {
        -String username
        -List~Project~ projectList
        +addProject(Project): void
        +removeProject(Project): void
        +getProjectList(): List~Project~
        +getUsername(): String
        +setUsername(String): void
    }

    class ActivityController {
        +addActivityToProject(Project, Activity): void
        +removeActivityFromProject(Project, Activity): void
        +getAllActivities(Project): List~Activity~
    }

    class MainViewController {
        -ListView~Activity~ activityListView
        -TextField titleField
        -Label activityIdLabel
        -TextArea descriptionArea
        -Button addButton
        -Button deleteButton
        -Button updateButton
        -DatePicker dueDatePicker
        -CheckBox completedCheckBox
        -ObservableList~Activity~ activityList
        -Project currentProject
        -Activity currentActivity
        +initialize(): void
        -handleActivitySelection(): void
        -handleAddActivity(): void
        -handleUpdateActivity(): void
        -handleDeleteActivity(): void
    }

    class MainView {
        +start(Stage): void
        +main(String[]): void
    }

    Project "1" --> "*" Activity : contains
    User "1" --> "*" Project : manages
    ActivityController ..> Activity : manages
    ActivityController ..> Project : manages
    MainViewController ..> ActivityController : uses
    MainViewController ..> Project : references
    MainView ..> MainViewController : uses
```

