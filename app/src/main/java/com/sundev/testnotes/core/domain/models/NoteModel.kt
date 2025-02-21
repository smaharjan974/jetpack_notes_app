package com.sundev.testnotes.core.domain.models

import com.sundev.testnotes.core.data.local.NoteEntity

data class NoteModel(
    val id: Int,
    val title: String,
    val description: String
)

fun NoteModel.toEntity(): NoteEntity {
    return NoteEntity(
        id = if (id == -1) null else id,
        title = title,
        description = description
    )
}

fun dummyNotes(): List<NoteModel> {
//    return listOf(
//        NoteModel(0,"Title 1","Description 1"),
//        NoteModel(0,"Title 2","Description 2"),
//        NoteModel(0,"Title 3","Description 3"),
//        NoteModel(0,"Title 4","Description 4"),
//        NoteModel(0,"Title 5","Description 5"),
//        NoteModel(0,"Title 6","Description 6"),
//        NoteModel(0,"Title 8","Description 7"),
//        NoteModel(0,"Title 9","Description 8"),
//        NoteModel(0,"Title 1","Description 9"),
//    )

    val items = arrayListOf<NoteModel>()
    for (i in 1..3) {
        items.add(NoteModel(i, "Title $i", "Descrption $i"))
    }
    return items
}