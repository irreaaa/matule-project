package com.example.model

object DataRepository {
    val userList = mutableListOf<User>()
    val sneakerList = mutableListOf<Sneacker>()


    init {
        userList.add(
            User(
                userId = 1,
                userName = "user",
                email = "u@mail.ru",
                password = "1234",
                favoriteSneakerIds = mutableListOf()
            )
        )
    }


    init {
        sneakerList.addAll(
            listOf(
                Sneacker(
                    id = 1,
                    name = "Nike Air Max",
                    description = "sneakers",
                    price = 732.0,
                    imageUrl = "mainsneakers",
                    category = "Tennis",
                    isPopular = true
                ),
                Sneacker(
                    id = 2,
                    name = "Nike Winflo",
                    description = "Running",
                    price = 850.0,
                    imageUrl = "mainsneakers",
                    category = "Running",
                    isPopular = false
                ),
                Sneacker(
                    id = 3,
                    name = "Nike Zoom Vomero",
                    description = "donkey",
                    price = 999.0,
                    imageUrl = "mainsneakers",
                    category = "Outdoor",
                    isPopular = true
                ),
                Sneacker(
                    id = 4,
                    name = "Nike Revolution",
                    description = "zebra",
                    price =239.0,
                    imageUrl = "mainsneakers",
                    category = "Tennis",
                    isPopular = false
                ),
                Sneacker(
                    id = 5,
                    name = "Nike Force",
                    description = "monkey",
                    price = 855.0,
                    imageUrl = "mainsneakers",
                    category = "Outdoor",
                    isPopular = true
                ),
                Sneacker(
                    id = 6,
                    name = "Nike Borough",
                    description = "pig",
                    price = 777.0,
                    imageUrl = "mainsneakers",
                    category = "Tennis",
                    isPopular = true
                )
            )
        )
    }

    fun findUserByEmailAndPassword(email: String, password: String): User? {
        return userList.firstOrNull { it.email == email && it.password == password }
    }
}