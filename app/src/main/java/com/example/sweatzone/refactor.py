import os, glob, re

files = glob.glob('Intermediate*WorkoutsScreen.kt')
files = [f for f in files if 'WorkoutsScreen' in f and 'IntermediateWorkoutsScreen.kt' not in f]

for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()

    muscle = ''
    if 'Abs' in f: muscle = 'abs'
    elif 'Arms' in f: muscle = 'arms'
    elif 'Back' in f: muscle = 'back'
    elif 'Chest' in f: muscle = 'chest'
    elif 'Legs' in f: muscle = 'legs'
    elif 'Shoulder' in f: muscle = 'shoulder'
    else: continue
    
    var_block = f'''
    var exercises by androidx.compose.runtime.remember {{ androidx.compose.runtime.mutableStateOf<List<com.example.sweatzone.data.dto.WorkoutExerciseDto>>(emptyList()) }}
    var isLoading by androidx.compose.runtime.remember {{ androidx.compose.runtime.mutableStateOf(true) }}
    var errorMessage by androidx.compose.runtime.remember {{ androidx.compose.runtime.mutableStateOf<String?>(null) }}

    androidx.compose.runtime.LaunchedEffect(Unit) {{
        try {{
            val response = com.example.sweatzone.data.api.RetrofitClient.api.getWorkoutExercises("{muscle}", "intermediate")
            if (response.isSuccessful) {{
                val body = response.body()
                if (body != null && body.status) {{
                    exercises = body.data ?: emptyList()
                }} else {{
                    errorMessage = body?.message ?: "Unknown API error"
                }}
            }} else {{
                errorMessage = "HTTP Error: ${{response.code()}}"
            }}
        }} catch (e: Exception) {{
            errorMessage = e.message ?: "Failed to connect to backend"
        }} finally {{
            isLoading = false
        }}
    }}
'''
    if 'var exercises by' not in content:
        content = re.sub(r'(val startTime = .*?System\.currentTimeMillis\(\) \})', r'\1\n' + var_block, content)

    imports = '''import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
'''
    if 'import androidx.compose.foundation.lazy.items' not in content:
        content = re.sub(r'(import .*?\n)', r'\1' + imports + '\n', content, count=1)

    pattern = r'(\s{24}\)\s*\n\s{20}\}\s*\n\s{16}\}\s*\n\s{12}\})(.*?)(?=(\s{12}item \{\s*\n\s{16}Button\())'
    
    dynamic_items = '''

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(exercises) { exercise ->
                    ExerciseItem(
                        showSetsTracker = true,
                        title = exercise.title,
                        videoUrl = "${com.example.sweatzone.data.api.RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                        instructions = exercise.instructions,
                        benefits = exercise.benefits
                    )
                }
            }
'''
    if 'items(exercises) {' not in content:
        content = re.sub(pattern, r'\1' + dynamic_items, content, flags=re.DOTALL)

    with open(f, 'w', encoding='utf-8') as file:
        file.write(content)

print(f"Refactored {len(files)} Intermediate Workouts.")
