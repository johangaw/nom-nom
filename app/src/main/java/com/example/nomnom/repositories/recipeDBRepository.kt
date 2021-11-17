package com.example.nomnom.repositories

import android.content.Context
import androidx.room.*
import com.example.nomnom.data.Recipe
import kotlinx.coroutines.flow.Flow

fun RecipeEntity.asRecipe(): Recipe {
    return Recipe(
        id = id,
        url = url,
        title = title,
        ingredients = ingredients.split("\n"),
        instructions = instructions.split("\n"),
        imageUri = imageUri,
    )
}

fun Recipe.asRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        url = this.url,
        title = this.title,
        ingredients = this.ingredients.joinToString("\n"),
        instructions = this.instructions.joinToString("\n"),
        imageUri = this.imageUri
    )
}

val RecipeEntity.isPersisted: Boolean
    get() = id > 0

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val url: String,
    val title: String,
    val instructions: String,
    val ingredients: String,
    val imageUri: String,
)


@Dao
interface RecipeDBRepository {
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getOne(id: Int): RecipeEntity

    @Query("SELECT * FROM recipes")
    fun observeAll(): Flow<List<RecipeEntity>>

    @Update
    suspend fun update(recipe: RecipeEntity)

    @Insert
    suspend fun create(recipe: RecipeEntity): Long

    @Delete
    suspend fun remove(recipe: RecipeEntity)
}


@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeRepository(): RecipeDBRepository

    companion object {
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            if (INSTANCE != null)
                return INSTANCE!!
            synchronized(this::class.java) {
                return INSTANCE
                    ?: Room.databaseBuilder(
                        context,
                        RecipeDatabase::class.java, "recipe-db"
                    ).build()
            }
        }
    }
}
