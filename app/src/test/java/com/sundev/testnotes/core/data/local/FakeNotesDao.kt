package com.sundev.testnotes.core.data.local

class FakeNotesDao(
    val items: ArrayList<NoteEntity>
) : NotesDao {
    override fun getAll(): List<NoteEntity> {
        return items
    }

    override fun get(id: Int): NoteEntity? {
        val itemIndex = items.indexOfFirst { it.id == id }
        if (itemIndex == -1) return null
        return items[itemIndex]
    }

    override fun insertItem(item: NoteEntity): Long {
        item.id?.let {  itemId ->

            val itemIndex = items.indexOfFirst { it.id == itemId }
            if(itemIndex == -1) return@let

            items[itemIndex] = item
            return itemId.toLong()

        }
        val newItemId = items.size + 1
        val updateItem = item.copy(
            id = newItemId
        )
        items.add(updateItem)
        return newItemId.toLong()
    }

    override fun update(item: NoteEntity) {
        val itemIndex = items.indexOfFirst { it.id == item.id }
        if (itemIndex == -1) return
        items[itemIndex] = item
    }

    override fun deleteItem(id: Int) {
        val itemIndex = items.indexOfFirst { it.id == id }
        if (itemIndex == -1) return
        items.removeAt(itemIndex)
    }
}