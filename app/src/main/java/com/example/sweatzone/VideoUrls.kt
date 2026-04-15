package com.example.sweatzone

import com.example.sweatzone.data.api.RetrofitClient

/**
 * Central registry for all workout video URLs.
 * Videos are served from the PHP backend.
 * To change the server IP, update BASE_URL in RetrofitClient.kt only.
 */
object VideoUrls {
    fun url(filename: String): String = "${RetrofitClient.BASE_URL}videos/$filename"

    // ── Arms ──────────────────────────────────────────────────────────────
    val bicepCurls            get() = url("bicep_curls_video.mp4")
    val hammerCurls           get() = url("hammer_curls_video.mp4")
    val ropePushdown          get() = url("rope_pushdown_video.mp4")
    val tricepExtension       get() = url("tricep_extension_video.mp4")
    val barbellCurl           get() = url("barbell_curl_video.mp4")
    val ezBarCurl             get() = url("ez_bar_curl_video.mp4")
    val preacherCurl          get() = url("preacher_curl_video.mp4")
    val inclineCurl           get() = url("incline_curl_video.mp4")
    val bayesianCurl          get() = url("bayesian_curl_video.mp4")
    val skullCrushers         get() = url("skull_crushers_video.mp4")
    val weightedDips          get() = url("weighted_dips_video.mp4")
    val closeGripBench        get() = url("close_grip_bench_video.mp4")
    val dips                  get() = url("dips_video.mp4")
    val weightedPullups       get() = url("weighted_pullups_video.mp4")
    val weightedPushup        get() = url("weighted_pushup_video.mp4")

    // ── Chest ─────────────────────────────────────────────────────────────
    val benchPress            get() = url("bench_press_video.mp4")
    val inclineBench          get() = url("incline_bench_video.mp4")
    val declineBenchPress     get() = url("decline_bench_press_video.mp4")
    val dumbbellBenchPress    get() = url("dumbbell_bench_press_video.mp4")
    val dumbbellFly           get() = url("dumbbell_fly_video.mp4")
    val cableCrossover        get() = url("cable_crossover_video.mp4")
    val butterflyPecDeck      get() = url("butterfly_pec_deck_video.mp4")
    val lowCableFly           get() = url("low_cable_fly_video.mp4")
    val dumbbellPullover      get() = url("dumbbell_pullover_video.mp4")
    val inclinePushup         get() = url("incline_pushup_video.mp4")
    val pushup                get() = url("pushup_video.mp4")
    val kneePushup            get() = url("knee_pushup_video.mp4")
    val chestStretch          get() = url("chest_stretch_video.mp4")
    val wideGripBench         get() = url("wide_grip_bench_video.mp4")

    // ── Back ──────────────────────────────────────────────────────────────
    val barbellRows           get() = url("barbell_rows_video.mp4")
    val dumbbellRow           get() = url("dumbbell_row_video.mp4")
    val dumbbellRows          get() = url("dumbbell_rows_video.mp4")
    val latPulldown           get() = url("lat_pulldown_video.mp4")
    val latPulldowns          get() = url("lat_pulldowns_video.mp4")
    val pullups               get() = url("pullups_video.mp4")
    val seatedRow             get() = url("seated_row_video.mp4")
    val chestSupportedRow     get() = url("chest_supported_row_video.mp4")
    val straightArmPulldown   get() = url("straight_arm_pulldown_video.mp4")
    val tbarRow               get() = url("tbar_row_video.mp4")
    val cableYRaises          get() = url("cable_y_raises_video.mp4")
    val hyperextensions       get() = url("hyperextensions_video.mp4")
    val backExtension         get() = url("back_extension_video.mp4")
    val deadlift              get() = url("deadlift_video.mp4")

    // ── Legs ──────────────────────────────────────────────────────────────
    val barbellSquat          get() = url("barbell_squat_video.mp4")
    val squats                get() = url("squats_video.mp4")
    val legPress              get() = url("leg_press_video.mp4")
    val legExtension          get() = url("leg_extension_video.mp4")
    val hamstringCurl         get() = url("hamstring_curl_video.mp4")
    val seatedHamstringCurl   get() = url("seated_hamstring_curl_video.mp4")
    val romanianDeadlift      get() = url("romanian_deadlift_video.mp4")
    val calfRaises            get() = url("calf_raises_video.mp4")
    val hackSquat             get() = url("hack_squat_video.mp4")
    val frontSquat            get() = url("front_squat_video.mp4")
    val jumpSquat             get() = url("jump_squat_video.mp4")
    val jumpingJacks          get() = url("jumping_jacks_video.mp4")
    val bulgarianSplitSquat   get() = url("bulgarian_split_squat_video.mp4")
    val nordicCurl            get() = url("nordic_curl_video.mp4")
    val sumoSquat             get() = url("sumo_squat_video.mp4")
    val pistolSquats          get() = url("pistol_squats_video.mp4")
    val walkingLunges         get() = url("walking_lunges_video.mp4")
    val gluteBridge           get() = url("glute_bridge_video.mp4")
    val singleLegGluteBridge  get() = url("single_leg_glute_bridge_video.mp4")
    val hamstringStretch      get() = url("hamstring_stretch_video.mp4")

    // ── Shoulders ─────────────────────────────────────────────────────────
    val overheadPress         get() = url("overhead_press_video.mp4")
    val shoulderPress         get() = url("shoulder_press_video.mp4")
    val lateralRaises         get() = url("lateral_raises_video.mp4")
    val facePulls             get() = url("face_pulls_video.mp4")
    val frontRaises           get() = url("front_raises_video.mp4")
    val rearDeltFly           get() = url("rear_delt_fly_video.mp4")
    val machineShoulderPress  get() = url("machine_shoulder_press_video.mp4")
    val cableLateralRaise     get() = url("cable_lateral_raise_video.mp4")
    val lateralRaiseDropset   get() = url("lateral_raise_dropset_video.mp4")
    val arnoldPress           get() = url("arnold_press_video.mp4")
    val arnoldPressStanding   get() = url("arnold_press_standing_video.mp4")
    val pushPress             get() = url("push_press_video.mp4")

    // ── Abs ───────────────────────────────────────────────────────────────
    val crunches              get() = url("crunches_video.mp4")
    val plank                 get() = url("plank_video.mp4")
    val russianTwists         get() = url("russian_twists_video.mp4")
    val legRaises             get() = url("leg_raises_video.mp4")
    val hangingLegRaises      get() = url("hanging_leg_raises_video.mp4")
    val hangingKneeRaises     get() = url("hanging_knee_raises_video.mp4")
    val cableCrunches         get() = url("cable_crunches_video.mp4")
    val declineSitups         get() = url("decline_situps_video.mp4")
    val heelTouches           get() = url("heel_touches_video.mp4")
    val abWheelRollout        get() = url("ab_wheel_rollout_video.mp4")
    val flutterKicks          get() = url("flutter_kicks_video.mp4")
    val kneeTucks             get() = url("knee_tucks_video.mp4")
    val dragonFlags           get() = url("dragon_flags_video.mp4")
    val reverseCrunches       get() = url("reverse_crunches_video.mp4")
    val sidePlank             get() = url("side_plank_video.mp4")
    val mountainClimbers      get() = url("mountain_climbers_video.mp4")
    val burpees               get() = url("burpees_video.mp4")
    val weightedRussianTwists get() = url("weighted_russian_twists_video.mp4")

    // ── Full Body / Home ──────────────────────────────────────────────────
    val highKnees             get() = url("high_knees_video.mp4")
    val handstandPushups      get() = url("handstand_pushups_video.mp4")
    val pikePushup            get() = url("pike_pushup_video.mp4")
}
