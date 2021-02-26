### User stories

#### Установка библиотеки

Пользователь скачивает .jar файл / скрипт для запуска и запускает его через командную строку. Если все необходимые зависимости установлены, пользователю предлагается выбрать папку, где будет храниться .jar файл библиотеки и отчёты. Затем он самостоятельно настраивает удобный ему доступ к скрипту запуска, чтобы вызывать его из командной строки.

#### Использование

Основной кейс выглядит примерно вот так: пользователь в командной строке выбирает корневую папку проекта или модуля и выполняет следующую команду:

`./collect-data.sh <папка с кодом> <режим> <.txt отчёт> <сохранить отчёт>`

Если конкретно:

`./collect-data.sh . --base --report --save-report`

В любом случае в консоль буден выведен отчёт по всему проекту в целом. Вот очень краткий пример:

```
Project tree:

My project
├── Dir one
│   ├── a.cpp
│   └── b.cpp
└── Dir two
    └── Subdir
        └── a.java

Total number of files: 3
Total number of lines: 80
```

Если указан флаг `--report`, то в текущую папку командной строки помещается файл `report.txt`.
Если указан флаг `--save-report`, то этот отчёт сохраняется в том числе и в базе данных.

#### История отчётов [OPTIONAL]

Также пользователь может запросить историю последних отчётов по данному проекту. В таком случае будет выведен список отчётов по образцу выше.

Напрмер команда:

`./get-history ~/projects/mySuperProject 3` выведет последние 3 отчёта.


#### Редактирование режимов [OPTIONAL]

Для начала пользователю необходимо сообщить, какие именно параметры можно изменять. Для этого можно выполнить вот такую команду:

`./set-mode --help`

На что будет выведен список вот таких флагов:

```
--lines             # show number of lines
--comments          # show number of comments, empty comments, filled comments
--classes           # show number of different classes
--ifs               # show number of if/else constructions
.
.
.
```

И, наконец, для создания нового режима нужно выполнить такую команду:

`./set-mode --name="<mode name>" --lines --ifs`

После выполнения этой команды пользователю станет доступно использование режима `<mode name>` наравне с `--base` и `--full`.