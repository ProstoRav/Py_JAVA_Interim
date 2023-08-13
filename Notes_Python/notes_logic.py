import json
import os
from datetime import datetime

current_dir = os.path.dirname(os.path.abspath(__file__))
notes_file = os.path.join(current_dir, "notes.json")

if not os.path.exists(notes_file):
    with open(notes_file, "w") as file:
        json.dump([], file)


def load_notes():
    with open(notes_file, "r") as load_file:
        return json.load(load_file)


def save_notes(notes):
    with open(notes_file, "w") as save_file:
        json.dump(notes, save_file, indent=4)


def create_note():
    title = input("Введите заголовок: ")
    body = input("Введите текст: ")

    notes = load_notes()
    note_id = len(notes) + 1

    note = {
        "id": note_id,
        "title": title,
        "body": body,
        "created_at": str(datetime.now()),
        "last_edit": str(datetime.now())
    }

    notes.append(note)
    save_notes(notes)
    print("Заметка успешно создана.")
    exit_choice()


def read_notes(filter_id=None, filter_date=None, filter_word=None):
    notes = load_notes()

    if filter_id:
        filtered_notes = [note for note in notes if note["id"] == filter_id]
    elif filter_date:
        try:
            filter_date = datetime.strptime(filter_date, "%Y-%m-%d").date()
            filtered_notes = [note for note in notes if datetime.strptime(note['created_at'],
                                                                          "%Y-%m-%d %H:%M:%S.%f").date() ==
                              filter_date]
        except ValueError:
            print("Неправильный формат даты. Выводятся на экран все заметки.")
            filtered_notes = notes
    elif filter_word:
        filtered_notes = [note for note in notes if filter_word.lower() in note["title"].lower() or
                          filter_word.lower() in note["body"].lower()]
    else:
        filtered_notes = notes

    if not filtered_notes:
        print("Заметки не найдены по указанному фильтру.")
    else:
        for note in filtered_notes:
            print(f"Идентификатор: {note['id']}")
            print(f"Заголовок: {note['title']}")
            print(f"Текст: {note['body']}")
            print(f"Создано: {note['created_at']}")
            print(f"Изменено: {note['last_edit']}")
            print()
    exit_choice()


def read_specific_note():
    filter_option = input("Выберите '1' чтобы найти заметку по идентификатору, '2' по дате создания заметки, "
                          "или '3' по ключевому слову: ")
    if filter_option == "1":
        note_id = int(input("Введите ID заметки: "))
        read_notes(filter_id=note_id)
    elif filter_option == "2":
        filter_date = input("Введите дату создания (YYYY-MM-DD) заметки: ")
        read_notes(filter_date=filter_date)
    elif filter_option == "3":
        filter_word = input("Введите ключевое слово: ")
        read_notes(filter_word=filter_word)
    else:
        print("Неправильный выбор. Выводятся на экран все заметки.")
        read_notes()


def update_note():
    try:
        note_id = int(input("Введите ID изменяемой заметки: "))
        notes = load_notes()
        found = False

        for note in notes:
            if note["id"] == note_id:
                new_title = input("Введите новый заголовок (или пропустите для сохранения существующего): ")
                new_body = input("Введите новый текст (или пропустите для сохранения существующего): ")

                if new_title:
                    note["title"] = new_title
                    note["last_edit"] = str(datetime.now())

                if new_body:
                    note["body"] = new_body
                    note["last_edit"] = str(datetime.now())

                save_notes(notes)
                print("Заметка успешно изменена.")
                found = True

        if not found:
            print("Заметка не найдена.")
        exit_choice()

    except ValueError:
        print()
        print("Такого ID не существует.")
        print()
        exit_choice()


def delete_note():
    try:
        note_id = int(input("Введите ID удаляемой заметки: "))
        notes = load_notes()

        note_index = None
        i = 0
        while i < len(notes):
            if notes[i]["id"] == note_id:
                note_index = i
                del notes[i]
            else:
                i += 1

        if note_index is not None:
            print("Заметка успешно удалена.")

            for i, note in enumerate(notes):
                note["id"] = i + 1

            save_notes(notes)

        else:
            print("Заметка не найдена.")
        exit_choice()

    except ValueError:
        print()
        print("Такого ID не существует.")
        print()
        exit_choice()


def exit_choice():
    choice = input("Нажмите '0' для выхода или любую другую клавишу для возврата в главное меню: ")
    if choice == "0":
        print()
        print("До свидания!")
        raise SystemExit
    else:
        main_menu()


def main_menu():
    print()
    print("Заметки")
    print("1. Создать заметку")
    print("2. Показать все заметки")
    print("3. Найти заметку")
    print("4. Изменить заметку")
    print("5. Удалить заметку")
    print("6. Выход")

    choice = input("Выберите нужную опцию (1-6): ")

    print()
    if choice == "1":
        create_note()
    elif choice == "2":
        read_notes()
    elif choice == "3":
        read_specific_note()
    elif choice == "4":
        update_note()
    elif choice == "5":
        delete_note()
    elif choice == "6":
        print("До свидания!")
        raise SystemExit
    else:
        print("Неправильный выбор, попробуйте снова.")
        main_menu()
