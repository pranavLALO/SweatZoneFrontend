import os
import re
import json

files = ['BeginnerAbsWorkoutsScreen.kt', 'BeginnerBackWorkoutsScreen.kt', 'BeginnerLegsWorkoutsScreen.kt', 'BeginnerArmsWorkoutsScreen.kt', 'BeginnerShoulderWorkoutsScreen.kt', 'BeginnerChestWorkoutsScreen.kt']
base_dir = r"d:\ProjectApp\Sweatzone4CURRENT\app\src\main\java\com\example\sweatzone"

all_exercises = []

for file in files:
    try:
        muscle = re.search(r'Beginner([A-Z][a-z]+)Workouts', file).group(1).lower()
        path = os.path.join(base_dir, file)
        if not os.path.exists(path):
            print(f"File not found: {path}")
            continue
            
        content = open(path, 'r', encoding='utf-8').read()
        
        # Regex to match the different ExerciseItem formats
        blocks = re.findall(r'(?:ExerciseItem|ArmsExerciseItem|ShoulderExerciseItem|LegsExerciseItem|BackExerciseItem|AbsExerciseItem)\s*\(\s*title\s*=\s*"([^"]+)"(.*?)\)(?=\s*(?:item|Button|Spacer|//|$))', content, re.DOTALL)
        
        for block in blocks:
            title = block[0].strip()
            rest = block[1]
            
            vid_filename = ""
            vid_res_match = re.search(r'videoResId\s*=\s*(?:R\.raw\.)?([a-zA-Z0-9_]+)', rest)
            vid_url_match = re.search(r'videoUrl\s*=\s*VideoUrls\.([a-zA-Z0-9_]+)', rest)
            
            if vid_res_match:
                vid_filename = vid_res_match.group(1) + '.mp4'
            elif vid_url_match:
                # Need to map VideoUrls to filename. Just make a best guess.
                prop = vid_url_match.group(1)
                vid_filename = prop + '_video.mp4' # typically this is the mapping
                
            inst_match = re.search(r'instructions\s*=\s*listOf\((.*?)\)', rest, re.DOTALL)
            instructions = []
            if inst_match:
                # Find all quoted strings inside the listOf block
                instructions = [s.strip() for s in re.findall(r'"([^"]+)"', inst_match.group(1))]
                
            ben_match = re.search(r'benefits\s*=\s*listOf\((.*?)\)', rest, re.DOTALL)
            benefits = []
            if ben_match:
                benefits = [s.strip() for s in re.findall(r'"([^"]+)"', ben_match.group(1))]
                
            all_exercises.append({
                'target_muscle': muscle,
                'difficulty': 'beginner',
                'title': title,
                'video_filename': vid_filename,
                'instructions': instructions,
                'benefits': benefits
            })
            print(f"Parsed {title}")
    except Exception as e:
        print(f"Error on {file}: {e}")

output_path = r"d:\ProjectApp\Sweatzone4CURRENT\extracted_exercises.json"
with open(output_path, 'w', encoding='utf-8') as f:
    json.dump(all_exercises, f, indent=2)
print(f"Saved {len(all_exercises)} exercises to {output_path}")
