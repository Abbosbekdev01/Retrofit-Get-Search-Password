package uz.abbosbek.retrofitlesson_2.retrofit

data class User(
    val id: Int,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val token: String,
)