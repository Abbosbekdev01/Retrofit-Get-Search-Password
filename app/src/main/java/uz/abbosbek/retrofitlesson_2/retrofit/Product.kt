package uz.abbosbek.retrofitlesson_2.retrofit

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage:Float,
    val rating: String,
    val stock: Float,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)
