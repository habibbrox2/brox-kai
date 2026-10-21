# Desktop-only ProGuard policy: shrink Compose, leave everything else alone.
#
# Rationale: Compose's UI / foundation / material3 / animation jars are ~20+ MB
# unminified and dominate the desktop distribution size. The rest of the
# classpath (Kotlin stdlib, Ktor, Coil, JNA, FileKit, DBus, Koin, lifecycle,
# navigation, kotlinx.coroutines, kotlinx.io, kotlinx.serialization, Okio,
# BouncyCastle, LiteRT, FreeTTS, SLF4J, the app itself, etc.) is small *and*
# each library has its own reflection / JNI / native-ABI pattern that ProGuard
# has repeatedly broken in practice:
#   - JNA resolves native symbols by exact method name  (#167, #175)
#   - kotlinx.coroutines' async builder fails JVM verification after ProGuard
#     method specialization
#   - Koin resolves dependencies by KClass identity
#   - DBus builds interface proxies by method name
#   - FileKit's XDG portal probe depends on DBus surviving intact (#173)
#   - Gson maps JSON fields by reflected field names
#   - BouncyCastle's signed JCE provider jar breaks if its classes are rewritten
# Rather than whitelist each library individually, this file whitelists the
# whole classpath and lets Compose be the only package ProGuard is allowed to
# rewrite. The shared proguard-rules.pro still carries the finer-grained rules
# needed by Android R8 and as secondary documentation; on desktop this file
# supersedes most of them in coverage.
-keep class !androidx.compose.**,!org.jetbrains.compose.**,** { *; }

# Runtime-visible annotations carry the @Serializable markers that
# kotlinx.serialization's serializer lookup (used by RouteSerializer in
# androidx.navigation's type-safe routes) reads via reflection; ProGuard strips
# optional class-file attributes by default, which empties the generated
# typeMap ("Route settings could not find any NavType ..."). Signature is
# required for generic serializer types, InnerClasses/EnclosingMethod for
# serializer companions living inside data classes.
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,AnnotationDefault,Signature,InnerClasses,EnclosingMethod

# Belt-and-braces: disable method specialization globally. Even inside the
# Compose packages we still shrink, the kotlinx.coroutines async/launch
# builders are called from Compose code paths and specialization there
# produces the same VerifyError ("Bad return type ... async$<hash>").
-optimizations !method/specialization/*
