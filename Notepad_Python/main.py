import json
import os
from datetime import datetime

notes_file = "notes.json"

# Check if notes file exists, otherwise create an empty one
if not os.path.exists(notes_file):
    with open(notes_file, "w") as file:
        json.dump([], file)


def load_notes():
    with open(notes_file, "r") as file:
        return json.load(file)


def save_notes(notes):
    with open(notes_file, "w") as file:
        json.dump(notes, file, indent=4)


def create_note():
    title = input("Enter note title: ")
    body = input("Enter note body: ")

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
    print("Note created successfully.")


def read_notes(filter_id=None, filter_date=None):
    notes = load_notes()
    filtered_notes = []

    if filter_id:
        filtered_notes = [note for note in notes if note["id"] == filter_id]
    elif filter_date:
        try:
            filter_date = datetime.strptime(filter_date, "%Y-%m-%d").date()
            filtered_notes = [note for note in notes if datetime.strptime(note['created_at'],
                                                                          "%Y-%m-%d %H:%M:%S.%f").date() ==
                              filter_date]
        except ValueError:
            print("Invalid date format. Showing all notes.")
            filtered_notes = notes
    else:
        filtered_notes = notes

    if not filtered_notes:
        print("No notes found for the given filter.")
    else:
        for note in filtered_notes:
            print(f"ID: {note['id']}")
            print(f"Title: {note['title']}")
            print(f"Body: {note['body']}")
            print(f"Created At: {note['created_at']}")
            print(f"Last Edit: {note['last_edit']}")
            print()


def update_note():
    note_id = int(input("Enter the ID of the note to update: "))
    notes = load_notes()
    found = False

    for note in notes:
        if note["id"] == note_id:
            new_title = input("Enter the new title (leave blank to keep the existing one): ")
            new_body = input("Enter the new body (leave blank to keep the existing one): ")

            if new_title:
                note["title"] = new_title
                note["last_edit"] = str(datetime.now())

            if new_body:
                note["body"] = new_body
                note["last_edit"] = str(datetime.now())

            save_notes(notes)
            print("Note updated successfully.")
            found = True
            break

    if not found:
        print("Note not found.")


def delete_note():
    note_id = int(input("Enter the ID of the note to delete: "))
    notes = load_notes()
    found = False

    for note in notes:
        if note["id"] == note_id:
            notes.remove(note)
            save_notes(notes)
            print("Note deleted successfully.")
            found = True
            break

    if not found:
        print("Note not found.")


# Main program loop
while True:
    print("Notes App")
    print("1. Create a new note")
    print("2. Read all notes")
    print("3. Show a specific note")
    print("4. Update a note")
    print("5. Delete a note")
    print("6. Exit")

    choice = input("Enter your choice (1-6): ")

    if choice == "1":
        create_note()
    elif choice == "2":
        read_notes()
    elif choice == "3":
        filter_option = input("Enter 'ID' to filter by note ID or 'DATE' to filter by note creation date: ")

        if filter_option.upper() == "ID":
            note_id = int(input("Enter the ID of the note to show: "))
            read_notes(filter_id=note_id)
        elif filter_option.upper() == "DATE":
            filter_date = input("Enter a date (YYYY-MM-DD) to filter the notes: ")
            read_notes(filter_date=filter_date)
        else:
            print("Invalid filter option. Showing all notes.")
            read_notes()
    elif choice == "4":
        update_note()
    elif choice == "5":
        delete_note()
    elif choice == "6":
        break
    else:
        print("Invalid choice. Please try again.")

print("Goodbye!")
