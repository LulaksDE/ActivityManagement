# ActivityManagement


### TODO:

- [X] Initiale Dateien anlegen
- [ ] 
- [ ]
- [ ] 
- [ ] 

## Projekt UML

``` mermaid

classDiagram
    class Activity {
        -String title
        -String description
        -LocalDate dueDate
        -boolean completed
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
        -ListView~String~ activityListView
        -TextField titleField
        -TextField descriptionField
        -Button addButton
        -Project currentProject
        +initialize(): void
        -handleAddActivity(): void
    }

    Project "1" --> "*" Activity : contains
    User "1" --> "*" Project : manages
    ActivityController ..> Activity : manages
    ActivityController ..> Project : manages
    MainViewController ..> ActivityController : uses
    MainViewController ..> Project : references

```

