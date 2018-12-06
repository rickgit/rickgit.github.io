## 编译ffmpeg

1. 下载ffmpeg
2. 解压文件夹，并添加权限
> chmod -R 777 ffmpeg-4.0.2

3. 检查 configure文件
修改configure文件
```
SLIBNAME_WITH_MAJOR='$(SLIBNAME).$(LIBMAJOR)'  
LIB_INSTALL_EXTRA_CMD='$$(RANLIB)"$(LIBDIR)/$(LIBNAME)"'  
SLIB_INSTALL_NAME='$(SLIBNAME_WITH_VERSION)'  
SLIB_INSTALL_LINKS='$(SLIBNAME_WITH_MAJOR)$(SLIBNAME)'  


替换为下面内容


SLIBNAME_WITH_MAJOR='$(SLIBPREF)$(FULLNAME)-$(LIBMAJOR)$(SLIBSUF)'
LIB_INSTALL_EXTRA_CMD='$$(RANLIB) "$(LIBDIR)/$(LIBNAME)"'
SLIB_INSTALL_NAME='$(SLIBNAME_WITH_MAJOR)'
SLIB_INSTALL_LINKS='$(SLIBNAME)'
```
> ./configure --help
```
Usage: configure [options]
Options: [defaults in brackets after descriptions]

Help options:
  --help                   print this message
  --quiet                  Suppress showing informative output
  --list-decoders          show all available decoders
  --list-encoders          show all available encoders
  --list-hwaccels          show all available hardware accelerators
  --list-demuxers          show all available demuxers
  --list-muxers            show all available muxers
  --list-parsers           show all available parsers
  --list-protocols         show all available protocols
  --list-bsfs              show all available bitstream filters
  --list-indevs            show all available input devices
  --list-outdevs           show all available output devices
  --list-filters           show all available filters

Standard options:
  --logfile=FILE           log tests and output to FILE [ffbuild/config.log]
  --disable-logging        do not log configure debug information
  --fatal-warnings         fail if any configure warning is generated
  --prefix=PREFIX          install in PREFIX [/usr/local]
  --bindir=DIR             install binaries in DIR [PREFIX/bin]
  --datadir=DIR            install data files in DIR [PREFIX/share/ffmpeg]
  --docdir=DIR             install documentation in DIR [PREFIX/share/doc/ffmpeg]
  --libdir=DIR             install libs in DIR [PREFIX/lib]
  --shlibdir=DIR           install shared libs in DIR [LIBDIR]
  --incdir=DIR             install includes in DIR [PREFIX/include]
  --mandir=DIR             install man page in DIR [PREFIX/share/man]
  --pkgconfigdir=DIR       install pkg-config files in DIR [LIBDIR/pkgconfig]
  --enable-rpath           use rpath to allow installing libraries in paths
                           not part of the dynamic linker search path
                           use rpath when linking programs (USE WITH CARE)
  --install-name-dir=DIR   Darwin directory name for installed targets

Licensing options:
  --enable-gpl             allow use of GPL code, the resulting libs
                           and binaries will be under GPL [no]
  --enable-version3        upgrade (L)GPL to version 3 [no]
  --enable-nonfree         allow use of nonfree code, the resulting libs
                           and binaries will be unredistributable [no]

Configuration options:
  --disable-static         do not build static libraries [no]
  --enable-shared          build shared libraries [no]
  --enable-small           optimize for size instead of speed
  --disable-runtime-cpudetect disable detecting CPU capabilities at runtime (smaller binary)
  --enable-gray            enable full grayscale support (slower color)
  --disable-swscale-alpha  disable alpha channel support in swscale
  --disable-all            disable building components, libraries and programs
  --disable-autodetect     disable automatically detected external libraries [no]

Program options:
  --disable-programs       do not build command line programs
  --disable-ffmpeg         disable ffmpeg build
  --disable-ffplay         disable ffplay build
  --disable-ffprobe        disable ffprobe build

Documentation options:
  --disable-doc            do not build documentation
  --disable-htmlpages      do not build HTML documentation pages
  --disable-manpages       do not build man documentation pages
  --disable-podpages       do not build POD documentation pages
  --disable-txtpages       do not build text documentation pages

Component options:
  --disable-avdevice       disable libavdevice build
  --disable-avcodec        disable libavcodec build
  --disable-avformat       disable libavformat build
  --disable-swresample     disable libswresample build
  --disable-swscale        disable libswscale build
  --disable-postproc       disable libpostproc build
  --disable-avfilter       disable libavfilter build
  --enable-avresample      enable libavresample build (deprecated) [no]
  --disable-pthreads       disable pthreads [autodetect]
  --disable-w32threads     disable Win32 threads [autodetect]
  --disable-os2threads     disable OS/2 threads [autodetect]
  --disable-network        disable network support [no]
  --disable-dct            disable DCT code
  --disable-dwt            disable DWT code
  --disable-error-resilience disable error resilience code
  --disable-lsp            disable LSP code
  --disable-lzo            disable LZO decoder code
  --disable-mdct           disable MDCT code
  --disable-rdft           disable RDFT code
  --disable-fft            disable FFT code
  --disable-faan           disable floating point AAN (I)DCT code
  --disable-pixelutils     disable pixel utils in libavutil

Individual component options:
  --disable-everything     disable all components listed below
  --disable-encoder=NAME   disable encoder NAME
  --enable-encoder=NAME    enable encoder NAME
  --disable-encoders       disable all encoders
  --disable-decoder=NAME   disable decoder NAME
  --enable-decoder=NAME    enable decoder NAME
  --disable-decoders       disable all decoders
  --disable-hwaccel=NAME   disable hwaccel NAME
  --enable-hwaccel=NAME    enable hwaccel NAME
  --disable-hwaccels       disable all hwaccels
  --disable-muxer=NAME     disable muxer NAME
  --enable-muxer=NAME      enable muxer NAME
  --disable-muxers         disable all muxers
  --disable-demuxer=NAME   disable demuxer NAME
  --enable-demuxer=NAME    enable demuxer NAME
  --disable-demuxers       disable all demuxers
  --enable-parser=NAME     enable parser NAME
  --disable-parser=NAME    disable parser NAME
  --disable-parsers        disable all parsers
  --enable-bsf=NAME        enable bitstream filter NAME
  --disable-bsf=NAME       disable bitstream filter NAME
  --disable-bsfs           disable all bitstream filters
  --enable-protocol=NAME   enable protocol NAME
  --disable-protocol=NAME  disable protocol NAME
  --disable-protocols      disable all protocols
  --enable-indev=NAME      enable input device NAME
  --disable-indev=NAME     disable input device NAME
  --disable-indevs         disable input devices
  --enable-outdev=NAME     enable output device NAME
  --disable-outdev=NAME    disable output device NAME
  --disable-outdevs        disable output devices
  --disable-devices        disable all devices
  --enable-filter=NAME     enable filter NAME
  --disable-filter=NAME    disable filter NAME
  --disable-filters        disable all filters

External library support:

  Using any of the following switches will allow FFmpeg to link to the
  corresponding external library. All the components depending on that library
  will become enabled, if all their other dependencies are met and they are not
  explicitly disabled. E.g. --enable-libwavpack will enable linking to
  libwavpack and allow the libwavpack encoder to be built, unless it is
  specifically disabled with --disable-encoder=libwavpack.

  Note that only the system libraries are auto-detected. All the other external
  libraries must be explicitly enabled.

  Also note that the following help text describes the purpose of the libraries
  themselves, not all their features will necessarily be usable by FFmpeg.

  --disable-alsa           disable ALSA support [autodetect]
  --disable-appkit         disable Apple AppKit framework [autodetect]
  --disable-avfoundation   disable Apple AVFoundation framework [autodetect]
  --enable-avisynth        enable reading of AviSynth script files [no]
  --disable-bzlib          disable bzlib [autodetect]
  --disable-coreimage      disable Apple CoreImage framework [autodetect]
  --enable-chromaprint     enable audio fingerprinting with chromaprint [no]
  --enable-frei0r          enable frei0r video filtering [no]
  --enable-gcrypt          enable gcrypt, needed for rtmp(t)e support
                           if openssl, librtmp or gmp is not used [no]
  --enable-gmp             enable gmp, needed for rtmp(t)e support
                           if openssl or librtmp is not used [no]
  --enable-gnutls          enable gnutls, needed for https support
                           if openssl or libtls is not used [no]
  --disable-iconv          disable iconv [autodetect]
  --enable-jni             enable JNI support [no]
  --enable-ladspa          enable LADSPA audio filtering [no]
  --enable-libaom          enable AV1 video encoding/decoding via libaom [no]
  --enable-libass          enable libass subtitles rendering,
                           needed for subtitles and ass filter [no]
  --enable-libbluray       enable BluRay reading using libbluray [no]
  --enable-libbs2b         enable bs2b DSP library [no]
  --enable-libcaca         enable textual display using libcaca [no]
  --enable-libcelt         enable CELT decoding via libcelt [no]
  --enable-libcdio         enable audio CD grabbing with libcdio [no]
  --enable-libcodec2       enable codec2 en/decoding using libcodec2 [no]
  --enable-libdc1394       enable IIDC-1394 grabbing using libdc1394
                           and libraw1394 [no]
  --enable-libfdk-aac      enable AAC de/encoding via libfdk-aac [no]
  --enable-libflite        enable flite (voice synthesis) support via libflite [no]
  --enable-libfontconfig   enable libfontconfig, useful for drawtext filter [no]
  --enable-libfreetype     enable libfreetype, needed for drawtext filter [no]
  --enable-libfribidi      enable libfribidi, improves drawtext filter [no]
  --enable-libgme          enable Game Music Emu via libgme [no]
  --enable-libgsm          enable GSM de/encoding via libgsm [no]
  --enable-libiec61883     enable iec61883 via libiec61883 [no]
  --enable-libilbc         enable iLBC de/encoding via libilbc [no]
  --enable-libjack         enable JACK audio sound server [no]
  --enable-libkvazaar      enable HEVC encoding via libkvazaar [no]
  --enable-libmodplug      enable ModPlug via libmodplug [no]
  --enable-libmp3lame      enable MP3 encoding via libmp3lame [no]
  --enable-libopencore-amrnb enable AMR-NB de/encoding via libopencore-amrnb [no]
  --enable-libopencore-amrwb enable AMR-WB decoding via libopencore-amrwb [no]
  --enable-libopencv       enable video filtering via libopencv [no]
  --enable-libopenh264     enable H.264 encoding via OpenH264 [no]
  --enable-libopenjpeg     enable JPEG 2000 de/encoding via OpenJPEG [no]
  --enable-libopenmpt      enable decoding tracked files via libopenmpt [no]
  --enable-libopus         enable Opus de/encoding via libopus [no]
  --enable-libpulse        enable Pulseaudio input via libpulse [no]
  --enable-librsvg         enable SVG rasterization via librsvg [no]
  --enable-librubberband   enable rubberband needed for rubberband filter [no]
  --enable-librtmp         enable RTMP[E] support via librtmp [no]
  --enable-libshine        enable fixed-point MP3 encoding via libshine [no]
  --enable-libsmbclient    enable Samba protocol via libsmbclient [no]
  --enable-libsnappy       enable Snappy compression, needed for hap encoding [no]
  --enable-libsoxr         enable Include libsoxr resampling [no]
  --enable-libspeex        enable Speex de/encoding via libspeex [no]
  --enable-libsrt          enable Haivision SRT protocol via libsrt [no]
  --enable-libssh          enable SFTP protocol via libssh [no]
  --enable-libtesseract    enable Tesseract, needed for ocr filter [no]
  --enable-libtheora       enable Theora encoding via libtheora [no]
  --enable-libtls          enable LibreSSL (via libtls), needed for https support
                           if openssl or gnutls is not used [no]
  --enable-libtwolame      enable MP2 encoding via libtwolame [no]
  --enable-libv4l2         enable libv4l2/v4l-utils [no]
  --enable-libvidstab      enable video stabilization using vid.stab [no]
  --enable-libvmaf         enable vmaf filter via libvmaf [no]
  --enable-libvo-amrwbenc  enable AMR-WB encoding via libvo-amrwbenc [no]
  --enable-libvorbis       enable Vorbis en/decoding via libvorbis,
                           native implementation exists [no]
  --enable-libvpx          enable VP8 and VP9 de/encoding via libvpx [no]
  --enable-libwavpack      enable wavpack encoding via libwavpack [no]
  --enable-libwebp         enable WebP encoding via libwebp [no]
  --enable-libx264         enable H.264 encoding via x264 [no]
  --enable-libx265         enable HEVC encoding via x265 [no]
  --enable-libxavs         enable AVS encoding via xavs [no]
  --enable-libxcb          enable X11 grabbing using XCB [autodetect]
  --enable-libxcb-shm      enable X11 grabbing shm communication [autodetect]
  --enable-libxcb-xfixes   enable X11 grabbing mouse rendering [autodetect]
  --enable-libxcb-shape    enable X11 grabbing shape rendering [autodetect]
  --enable-libxvid         enable Xvid encoding via xvidcore,
                           native MPEG-4/Xvid encoder exists [no]
  --enable-libxml2         enable XML parsing using the C library libxml2 [no]
  --enable-libzimg         enable z.lib, needed for zscale filter [no]
  --enable-libzmq          enable message passing via libzmq [no]
  --enable-libzvbi         enable teletext support via libzvbi [no]
  --enable-lv2             enable LV2 audio filtering [no]
  --disable-lzma           disable lzma [autodetect]
  --enable-decklink        enable Blackmagic DeckLink I/O support [no]
  --enable-libndi_newtek   enable Newteck NDI I/O support [no]
  --enable-mediacodec      enable Android MediaCodec support [no]
  --enable-libmysofa       enable libmysofa, needed for sofalizer filter [no]
  --enable-openal          enable OpenAL 1.1 capture support [no]
  --enable-opencl          enable OpenCL processing [no]
  --enable-opengl          enable OpenGL rendering [no]
  --enable-openssl         enable openssl, needed for https support
                           if gnutls or libtls is not used [no]
  --disable-sndio          disable sndio support [autodetect]
  --disable-schannel       disable SChannel SSP, needed for TLS support on
                           Windows if openssl and gnutls are not used [autodetect]
  --disable-sdl2           disable sdl2 [autodetect]
  --disable-securetransport disable Secure Transport, needed for TLS support
                           on OSX if openssl and gnutls are not used [autodetect]
  --disable-xlib           disable xlib [autodetect]
  --disable-zlib           disable zlib [autodetect]

  The following libraries provide various hardware acceleration features:
  --disable-amf            disable AMF video encoding code [autodetect]
  --disable-audiotoolbox   disable Apple AudioToolbox code [autodetect]
  --enable-cuda-sdk        enable CUDA features that require the CUDA SDK [no]
  --disable-cuvid          disable Nvidia CUVID support [autodetect]
  --disable-d3d11va        disable Microsoft Direct3D 11 video acceleration code [autodetect]
  --disable-dxva2          disable Microsoft DirectX 9 video acceleration code [autodetect]
  --disable-ffnvcodec      disable dynamically linked Nvidia code [autodetect]
  --enable-libdrm          enable DRM code (Linux) [no]
  --enable-libmfx          enable Intel MediaSDK (AKA Quick Sync Video) code via libmfx [no]
  --enable-libnpp          enable Nvidia Performance Primitives-based code [no]
  --enable-mmal            enable Broadcom Multi-Media Abstraction Layer (Raspberry Pi) via MMAL [no]
  --disable-nvdec          disable Nvidia video decoding acceleration (via hwaccel) [autodetect]
  --disable-nvenc          disable Nvidia video encoding code [autodetect]
  --enable-omx             enable OpenMAX IL code [no]
  --enable-omx-rpi         enable OpenMAX IL code for Raspberry Pi [no]
  --enable-rkmpp           enable Rockchip Media Process Platform code [no]
  --disable-v4l2-m2m       disable V4L2 mem2mem code [autodetect]
  --disable-vaapi          disable Video Acceleration API (mainly Unix/Intel) code [autodetect]
  --disable-vdpau          disable Nvidia Video Decode and Presentation API for Unix code [autodetect]
  --disable-videotoolbox   disable VideoToolbox code [autodetect]

Toolchain options:
  --arch=ARCH              select architecture []
  --cpu=CPU                select the minimum required CPU (affects
                           instruction selection, may crash on older CPUs)
  --cross-prefix=PREFIX    use PREFIX for compilation tools []
  --progs-suffix=SUFFIX    program name suffix []
  --enable-cross-compile   assume a cross-compiler is used
  --sysroot=PATH           root of cross-build tree
  --sysinclude=PATH        location of cross-build system headers
  --target-os=OS           compiler targets OS []
  --target-exec=CMD        command to run executables on target
  --target-path=DIR        path to view of build directory on target
  --target-samples=DIR     path to samples directory on target
  --tempprefix=PATH        force fixed dir/prefix instead of mktemp for checks
  --toolchain=NAME         set tool defaults according to NAME
                           (gcc-asan, clang-asan, gcc-msan, clang-msan,
                           gcc-tsan, clang-tsan, gcc-usan, clang-usan,
                           valgrind-massif, valgrind-memcheck,
                           msvc, icl, gcov, llvm-cov, hardened)
  --nm=NM                  use nm tool NM [nm -g]
  --ar=AR                  use archive tool AR [ar]
  --as=AS                  use assembler AS []
  --ln_s=LN_S              use symbolic link tool LN_S [ln -s -f]
  --strip=STRIP            use strip tool STRIP [strip]
  --windres=WINDRES        use windows resource compiler WINDRES [windres]
  --x86asmexe=EXE          use nasm-compatible assembler EXE [nasm]
  --cc=CC                  use C compiler CC [gcc]
  --cxx=CXX                use C compiler CXX [g++]
  --objcc=OCC              use ObjC compiler OCC [gcc]
  --dep-cc=DEPCC           use dependency generator DEPCC [gcc]
  --nvcc=NVCC              use Nvidia CUDA compiler NVCC [nvcc]
  --ld=LD                  use linker LD []
  --pkg-config=PKGCONFIG   use pkg-config tool PKGCONFIG [pkg-config]
  --pkg-config-flags=FLAGS pass additional flags to pkgconf []
  --ranlib=RANLIB          use ranlib RANLIB [ranlib]
  --doxygen=DOXYGEN        use DOXYGEN to generate API doc [doxygen]
  --host-cc=HOSTCC         use host C compiler HOSTCC
  --host-cflags=HCFLAGS    use HCFLAGS when compiling for host
  --host-cppflags=HCPPFLAGS use HCPPFLAGS when compiling for host
  --host-ld=HOSTLD         use host linker HOSTLD
  --host-ldflags=HLDFLAGS  use HLDFLAGS when linking for host
  --host-libs=HLIBS        use libs HLIBS when linking for host
  --host-os=OS             compiler host OS []
  --extra-cflags=ECFLAGS   add ECFLAGS to CFLAGS []
  --extra-cxxflags=ECFLAGS add ECFLAGS to CXXFLAGS []
  --extra-objcflags=FLAGS  add FLAGS to OBJCFLAGS []
  --extra-ldflags=ELDFLAGS add ELDFLAGS to LDFLAGS []
  --extra-ldexeflags=ELDFLAGS add ELDFLAGS to LDEXEFLAGS []
  --extra-ldsoflags=ELDFLAGS add ELDFLAGS to LDSOFLAGS []
  --extra-libs=ELIBS       add ELIBS []
  --extra-version=STRING   version string suffix []
  --optflags=OPTFLAGS      override optimization-related compiler flags
  --nvccflags=NVCCFLAGS    override nvcc flags [-gencode arch=compute_30,code=sm_30 -O2]
  --build-suffix=SUFFIX    library name suffix []
  --enable-pic             build position-independent code
  --enable-thumb           compile for Thumb instruction set
  --enable-lto             use link-time optimization
  --env="ENV=override"     override the environment variables

Advanced options (experts only):
  --malloc-prefix=PREFIX   prefix malloc and related names with PREFIX
  --custom-allocator=NAME  use a supported custom allocator
  --disable-symver         disable symbol versioning
  --enable-hardcoded-tables use hardcoded tables instead of runtime generation
  --disable-safe-bitstream-reader
                           disable buffer boundary checking in bitreaders
                           (faster, but may crash)
  --sws-max-filter-size=N  the max filter size swscale uses [256]

Optimization options (experts only):
  --disable-asm            disable all assembly optimizations
  --disable-altivec        disable AltiVec optimizations
  --disable-vsx            disable VSX optimizations
  --disable-power8         disable POWER8 optimizations
  --disable-amd3dnow       disable 3DNow! optimizations
  --disable-amd3dnowext    disable 3DNow! extended optimizations
  --disable-mmx            disable MMX optimizations
  --disable-mmxext         disable MMXEXT optimizations
  --disable-sse            disable SSE optimizations
  --disable-sse2           disable SSE2 optimizations
  --disable-sse3           disable SSE3 optimizations
  --disable-ssse3          disable SSSE3 optimizations
  --disable-sse4           disable SSE4 optimizations
  --disable-ss e42          disable SSE4.2 optimizations
  --disable-avx            disable AVX optimizations
  --disable-xop            disable XOP optimizations
  --disable-fma3           disable FMA3 optimizations
  --disable-fma4           disable FMA4 optimizations
  --disable-avx2           disable AVX2 optimizations
  --disable-avx512         disable AVX-512 optimizations
  --disable-aesni          disable AESNI optimizations
  --disable-armv5te        disable armv5te optimizations
  --disable-armv6          disable armv6 optimizations
  --disable-armv6t2        disable armv6t2 optimizations
  --disable-vfp            disable VFP optimizations
  --disable-neon           disable NEON optimizations
  --disable-inline-asm     disable use of inline assembly
  --disable-x86asm         disable use of standalone x86 assembly
  --disable-mipsdsp        disable MIPS DSP ASE R1 optimizations
  --disable-mipsdspr2      disable MIPS DSP ASE R2 optimizations
  --disable-msa            disable MSA optimizations
  --disable-mipsfpu        disable floating point MIPS optimizations
  --disable-mmi            disable Loongson SIMD optimizations
  --disable-fast-unaligned consider unaligned accesses slow

Developer options (useful when working on FFmpeg itself):
  --disable-debug          disable debugging symbols
  --enable-debug=LEVEL     set the debug level []
  --disable-optimizations  disable compiler optimizations
  --enable-extra-warnings  enable more compiler warnings
  --disable-stripping      disable stripping of executables and shared libraries
  --assert-level=level     0(default), 1 or 2, amount of assertion testing,
                           2 causes a slowdown at runtime.
  --enable-memory-poisoning fill heap uninitialized allocated space with arbitrary data
  --valgrind=VALGRIND      run "make fate" tests through valgrind to detect memory
                           leaks and errors, using the specified valgrind binary.
                           Cannot be combined with --target-exec
  --enable-ftrapv          Trap arithmetic overflows
  --samples=PATH           location of test samples for FATE, if not set use
                           $FATE_SAMPLES at make invocation time.
  --enable-neon-clobber-test check NEON registers for clobbering (should be
                           used only for debugging purposes)
  --enable-xmm-clobber-test check XMM registers for clobbering (Win64-only;
                           should be used only for debugging purposes)
  --enable-random          randomly enable/disable components
  --disable-random
  --enable-random=LIST     randomly enable/disable specific components or
  --disable-random=LIST    component groups. LIST is a comma-separated list
                           of NAME[:PROB] entries where NAME is a component
                           (group) and PROB the probability associated with
                           NAME (default 0.5).
  --random-seed=VALUE      seed value for --enable/disable-random
  --disable-valgrind-backtrace do not print a backtrace under Valgrind
                           (only applies to --disable-optimizations builds)
  --enable-osfuzz          Enable building fuzzer tool
  --libfuzzer=PATH         path to libfuzzer
  --ignore-tests=TESTS     comma-separated list (without "fate-" prefix
                           in the name) of tests whose result is ignored
  --enable-linux-perf      enable Linux Performance Monitor API

NOTE: Object files are built at the place where configure is launched.

```

> chmod 777 configure
> ./configure

出现以下问题，及解决方法
```
gcc is unable to create an executable file.
If gcc is a cross-compiler, use the --enable-cross-compile option.
Only do this if you know what cross compiling means.
C compiler test failed.

If you think configure made a mistake, make sure you are using the latest
version from Git.  If the latest version fails, report the problem to the
ffmpeg-user@ffmpeg.org mailing list or IRC #ffmpeg on irc.freenode.net.
Include the log file "ffbuild/config.log" produced by configure as this will help
solve the problem.

------------------------------------------------------------------

安装gcc

sudo apt install gcc make yasm
```

出现以下问题，及解决方法
```
nasm/yasm not found or too old. Use --disable-x86asm for a crippled build.

If you think configure made a mistake, make sure you are using the latest
version from Git.  If the latest version fails, report the problem to the
ffmpeg-user@ffmpeg.org mailing list or IRC #ffmpeg on irc.freenode.net.
Include the log file "ffbuild/config.log" produced by configure as this will help
solve the problem.


-----------------------------------

./configure --disable-yasm
或
./configure --disable-x86asm

```

成功日志

```
install prefix            /usr/local
source path               .
C compiler                gcc
C library                 glibc
ARCH                      x86 (generic)
big-endian                no
runtime cpu detection     yes
standalone assembly       no
x86 assembler             nasm
MMX enabled               yes
MMXEXT enabled            yes
3DNow! enabled            yes
3DNow! extended enabled   yes
SSE enabled               yes
SSSE3 enabled             yes
AESNI enabled             yes
AVX enabled               yes
AVX2 enabled              yes
AVX-512 enabled           yes
XOP enabled               yes
FMA3 enabled              yes
FMA4 enabled              yes
i686 features enabled     yes
CMOV is fast              yes
EBX available             yes
EBP available             yes
debug symbols             yes
strip symbols             yes
optimize for size         no
optimizations             yes
static                    yes
shared                    no
postprocessing support    no
network support           yes
threading support         pthreads
safe bitstream reader     yes
texi2html enabled         no
perl enabled              yes
pod2man enabled           yes
makeinfo enabled          no
makeinfo supports HTML    no

External libraries:
iconv			   xlib

External libraries providing hardware acceleration:
v4l2_m2m

Libraries:
avcodec			   avformat		      swresample
avdevice		   avutil		      swscale
avfilter

Programs:
ffmpeg			   ffprobe

Enabled decoders:
aac			   atrac3pal		      evrc
aac_fixed		   aura			      ffv1
aac_latm		   aura2		      ffvhuff
aasc			   avrn			      ffwavesynth
ac3			   avrp			      fic
ac3_fixed		   avs			      fits
adpcm_4xm		   avui			      flac
adpcm_adx		   ayuv			      flic
adpcm_afc		   bethsoftvid		      flv
adpcm_aica		   bfi			      fmvc
adpcm_ct		   bink			      fourxm
adpcm_dtk		   binkaudio_dct	      fraps
adpcm_ea		   binkaudio_rdft	      frwu
adpcm_ea_maxis_xa	   bintext		      g723_1
adpcm_ea_r1		   bitpacked		      g729
adpcm_ea_r2		   bmp			      gdv
adpcm_ea_r3		   bmv_audio		      gif
adpcm_ea_xas		   bmv_video		      gremlin_dpcm
adpcm_g722		   brender_pix		      gsm
adpcm_g726		   c93			      gsm_ms
adpcm_g726le		   cavs			      h261
adpcm_ima_amv		   ccaption		      h263
adpcm_ima_apc		   cdgraphics		      h263_v4l2m2m
adpcm_ima_dat4		   cdxl			      h263i
adpcm_ima_dk3		   cfhd			      h263p
adpcm_ima_dk4		   cinepak		      h264
adpcm_ima_ea_eacs	   clearvideo		      h264_v4l2m2m
adpcm_ima_ea_sead	   cljr			      hap
adpcm_ima_iss		   cllc			      hevc
adpcm_ima_oki		   comfortnoise		      hnm4_video
adpcm_ima_qt		   cook			      hq_hqa
adpcm_ima_rad		   cpia			      hqx
adpcm_ima_smjpeg	   cscd			      huffyuv
adpcm_ima_wav		   cyuv			      iac
adpcm_ima_ws		   dca			      idcin
adpcm_ms		   dds			      idf
adpcm_mtaf		   dfa			      iff_ilbm
adpcm_psx		   dirac		      imc
adpcm_sbpro_2		   dnxhd		      indeo2
adpcm_sbpro_3		   dolby_e		      indeo3
adpcm_sbpro_4		   dpx			      indeo4
adpcm_swf		   dsd_lsbf		      indeo5
adpcm_thp		   dsd_lsbf_planar	      interplay_acm
adpcm_thp_le		   dsd_msbf		      interplay_dpcm
adpcm_vima		   dsd_msbf_planar	      interplay_video
adpcm_xa		   dsicinaudio		      jacosub
adpcm_yamaha		   dsicinvideo		      jpeg2000
aic			   dss_sp		      jpegls
alac			   dst			      jv
alias_pix		   dvaudio		      kgv1
als			   dvbsub		      kmvc
amrnb			   dvdsub		      lagarith
amrwb			   dvvideo		      loco
amv			   dxtory		      m101
anm			   dxv			      mace3
ansi			   eac3			      mace6
ape			   eacmv		      magicyuv
aptx			   eamad		      mdec
aptx_hd			   eatgq		      metasound
ass			   eatgv		      microdvd
asv1			   eatqi		      mimic
asv2			   eightbps		      mjpeg
atrac1			   eightsvx_exp		      mjpegb
atrac3			   eightsvx_fib		      mlp
atrac3al		   escape124		      mmvideo
atrac3p			   escape130		      motionpixels
movtext			   pcm_s8		      subrip
mp1			   pcm_s8_planar	      subviewer
mp1float		   pcm_u16be		      subviewer1
mp2			   pcm_u16le		      sunrast
mp2float		   pcm_u24be		      svq1
mp3			   pcm_u24le		      svq3
mp3adu			   pcm_u32be		      tak
mp3adufloat		   pcm_u32le		      targa
mp3float		   pcm_u8		      targa_y216
mp3on4			   pcm_zork		      text
mp3on4float		   pcx			      theora
mpc7			   pgm			      thp
mpc8			   pgmyuv		      tiertexseqvideo
mpeg1_v4l2m2m		   pgssub		      tiff
mpeg1video		   pictor		      tmv
mpeg2_v4l2m2m		   pixlet		      truehd
mpeg2video		   pjs			      truemotion1
mpeg4			   ppm			      truemotion2
mpeg4_v4l2m2m		   prores		      truemotion2rt
mpegvideo		   prores_lgpl		      truespeech
mpl2			   psd			      tscc2
msa1			   ptx			      tta
msmpeg4v1		   qcelp		      twinvq
msmpeg4v2		   qdm2			      txd
msmpeg4v3		   qdmc			      ulti
msrle			   qdraw		      utvideo
mss1			   qpeg			      v210
mss2			   qtrle		      v210x
msvideo1		   r10k			      v308
mszh			   r210			      v408
mts2			   ra_144		      v410
mvc1			   ra_288		      vb
mvc2			   ralf			      vble
mxpeg			   rawvideo		      vc1
nellymoser		   realtext		      vc1_v4l2m2m
nuv			   rl2			      vc1image
on2avc			   roq			      vcr1
opus			   roq_dpcm		      vmdaudio
paf_audio		   rpza			      vmdvideo
paf_video		   rv10			      vmnc
pam			   rv20			      vorbis
pbm			   rv30			      vp3
pcm_alaw		   rv40			      vp5
pcm_bluray		   s302m		      vp6
pcm_dvd			   sami			      vp6a
pcm_f16le		   sanm			      vp6f
pcm_f24le		   sbc			      vp7
pcm_f32be		   scpr			      vp8
pcm_f32le		   sdx2_dpcm		      vp8_v4l2m2m
pcm_f64be		   sgi			      vp9
pcm_f64le		   sgirle		      vp9_v4l2m2m
pcm_lxf			   sheervideo		      vplayer
pcm_mulaw		   shorten		      vqa
pcm_s16be		   sipr			      wavpack
pcm_s16be_planar	   smackaud		      webp
pcm_s16le		   smacker		      webvtt
pcm_s16le_planar	   smc			      wmalossless
pcm_s24be		   smvjpeg		      wmapro
pcm_s24daud		   snow			      wmav1
pcm_s24le		   sol_dpcm		      wmav2
pcm_s24le_planar	   sonic		      wmavoice
pcm_s32be		   sp5x			      wmv1
pcm_s32le		   speedhq		      wmv2
pcm_s32le_planar	   srt			      wmv3
pcm_s64be		   ssa			      wmv3image
pcm_s64le		   stl			      wnv1
wrapped_avframe		   xface		      xwd
ws_snd1			   xl			      y41p
xan_dpcm		   xma1			      ylc
xan_wc3			   xma2			      yop
xan_wc4			   xpm			      yuv4
xbin			   xsub			      zero12v
xbm

Enabled encoders:
a64multi		   jpegls		      ppm
a64multi5		   ljpeg		      prores
aac			   magicyuv		      prores_aw
ac3			   mjpeg		      prores_ks
ac3_fixed		   mlp			      qtrle
adpcm_adx		   movtext		      r10k
adpcm_g722		   mp2			      r210
adpcm_g726		   mp2fixed		      ra_144
adpcm_g726le		   mpeg1video		      rawvideo
adpcm_ima_qt		   mpeg2video		      roq
adpcm_ima_wav		   mpeg4		      roq_dpcm
adpcm_ms		   mpeg4_v4l2m2m	      rv10
adpcm_swf		   msmpeg4v2		      rv20
adpcm_yamaha		   msmpeg4v3		      s302m
alac			   msvideo1		      sbc
alias_pix		   nellymoser		      sgi
amv			   opus			      snow
aptx			   pam			      sonic
aptx_hd			   pbm			      sonic_ls
ass			   pcm_alaw		      srt
asv1			   pcm_f32be		      ssa
asv2			   pcm_f32le		      subrip
avrp			   pcm_f64be		      sunrast
avui			   pcm_f64le		      svq1
ayuv			   pcm_mulaw		      targa
bmp			   pcm_s16be		      text
cinepak			   pcm_s16be_planar	      tiff
cljr			   pcm_s16le		      truehd
comfortnoise		   pcm_s16le_planar	      tta
dca			   pcm_s24be		      utvideo
dnxhd			   pcm_s24daud		      v210
dpx			   pcm_s24le		      v308
dvbsub			   pcm_s24le_planar	      v408
dvdsub			   pcm_s32be		      v410
dvvideo			   pcm_s32le		      vc2
eac3			   pcm_s32le_planar	      vorbis
ffv1			   pcm_s64be		      vp8_v4l2m2m
ffvhuff			   pcm_s64le		      wavpack
fits			   pcm_s8		      webvtt
flac			   pcm_s8_planar	      wmav1
flv			   pcm_u16be		      wmav2
g723_1			   pcm_u16le		      wmv1
gif			   pcm_u24be		      wmv2
h261			   pcm_u24le		      wrapped_avframe
h263			   pcm_u32be		      xbm
h263_v4l2m2m		   pcm_u32le		      xface
h263p			   pcm_u8		      xsub
h264_v4l2m2m		   pcx			      xwd
huffyuv			   pgm			      y41p
jpeg2000		   pgmyuv		      yuv4

Enabled hwaccels:

Enabled parsers:
aac			   dvdsub		      png
aac_latm		   flac			      pnm
ac3			   g729			      rv30
adx			   gsm			      rv40
bmp			   h261			      sbc
cavsvideo		   h263			      sipr
cook			   h264			      tak
dca			   hevc			      vc1
dirac			   mjpeg		      vorbis
dnxhd			   mlp			      vp3
dpx			   mpeg4video		      vp8
dvaudio			   mpegaudio		      vp9
dvbsub			   mpegvideo		      xma
dvd_nav			   opus

Enabled demuxers:
aa			   eac3			      jacosub
aac			   epaf			      jv
ac3			   ffmetadata		      live_flv
acm			   filmstrip		      lmlm4
act			   fits			      loas
adf			   flac			      lrc
adp			   flic			      lvf
ads			   flv			      lxf
adx			   fourxm		      m4v
aea			   frm			      matroska
afc			   fsb			      mgsts
aiff			   g722			      microdvd
aix			   g723_1		      mjpeg
amr			   g726			      mjpeg_2000
amrnb			   g726le		      mlp
amrwb			   g729			      mlv
anm			   gdv			      mm
apc			   genh			      mmf
ape			   gif			      mov
apng			   gsm			      mp3
aptx			   gxf			      mpc
aptx_hd			   h261			      mpc8
aqtitle			   h263			      mpegps
asf			   h264			      mpegts
asf_o			   hevc			      mpegtsraw
ass			   hls			      mpegvideo
ast			   hnm			      mpjpeg
au			   ico			      mpl2
avi			   idcin		      mpsub
avr			   idf			      msf
avs			   iff			      msnwc_tcp
bethsoftvid		   ilbc			      mtaf
bfi			   image2		      mtv
bfstm			   image2_alias_pix	      musx
bink			   image2_brender_pix	      mv
bintext			   image2pipe		      mvi
bit			   image_bmp_pipe	      mxf
bmv			   image_dds_pipe	      mxg
boa			   image_dpx_pipe	      nc
brstm			   image_exr_pipe	      nistsphere
c93			   image_j2k_pipe	      nsp
caf			   image_jpeg_pipe	      nsv
cavsvideo		   image_jpegls_pipe	      nut
cdg			   image_pam_pipe	      nuv
cdxl			   image_pbm_pipe	      ogg
cine			   image_pcx_pipe	      oma
codec2			   image_pgm_pipe	      paf
codec2raw		   image_pgmyuv_pipe	      pcm_alaw
concat			   image_pictor_pipe	      pcm_f32be
data			   image_png_pipe	      pcm_f32le
daud			   image_ppm_pipe	      pcm_f64be
dcstr			   image_psd_pipe	      pcm_f64le
dfa			   image_qdraw_pipe	      pcm_mulaw
dirac			   image_sgi_pipe	      pcm_s16be
dnxhd			   image_sunrast_pipe	      pcm_s16le
dsf			   image_svg_pipe	      pcm_s24be
dsicin			   image_tiff_pipe	      pcm_s24le
dss			   image_webp_pipe	      pcm_s32be
dts			   image_xpm_pipe	      pcm_s32le
dtshd			   ingenient		      pcm_s8
dv			   ipmovie		      pcm_u16be
dvbsub			   ircam		      pcm_u16le
dvbtxt			   iss			      pcm_u24be
dxa			   iv8			      pcm_u24le
ea			   ivf			      pcm_u32be
ea_cdata		   ivr			      pcm_u32le
pcm_u8			   shorten		      v210x
pjs			   siff			      vag
pmp			   sln			      vc1
pva			   smacker		      vc1t
pvf			   smjpeg		      vivo
qcp			   smush		      vmd
r3d			   sol			      vobsub
rawvideo		   sox			      voc
realtext		   spdif		      vpk
redspark		   srt			      vplayer
rl2			   stl			      vqf
rm			   str			      w64
roq			   subviewer		      wav
rpl			   subviewer1		      wc3
rsd			   sup			      webm_dash_manifest
rso			   svag			      webvtt
rtp			   swf			      wsaud
rtsp			   tak			      wsd
s337m			   tedcaptions		      wsvqa
sami			   thp			      wtv
sap			   threedostr		      wv
sbc			   tiertexseq		      wve
sbg			   tmv			      xa
scc			   truehd		      xbin
sdp			   tta			      xmv
sdr2			   tty			      xvag
sds			   txd			      xwma
sdx			   ty			      yop
segafilm		   v210			      yuv4mpegpipe

Enabled muxers:
a64			   hls			      pcm_s24le
ac3			   ico			      pcm_s32be
adts			   ilbc			      pcm_s32le
adx			   image2		      pcm_s8
aiff			   image2pipe		      pcm_u16be
amr			   ipod			      pcm_u16le
apng			   ircam		      pcm_u24be
aptx			   ismv			      pcm_u24le
aptx_hd			   ivf			      pcm_u32be
asf			   jacosub		      pcm_u32le
asf_stream		   latm			      pcm_u8
ass			   lrc			      psp
ast			   m4v			      rawvideo
au			   matroska		      rm
avi			   matroska_audio	      roq
avm2			   md5			      rso
bit			   microdvd		      rtp
caf			   mjpeg		      rtp_mpegts
cavsvideo		   mkvtimestamp_v2	      rtsp
codec2			   mlp			      sap
codec2raw		   mmf			      sbc
crc			   mov			      scc
dash			   mp2			      segafilm
data			   mp3			      segment
daud			   mp4			      singlejpeg
dirac			   mpeg1system		      smjpeg
dnxhd			   mpeg1vcd		      smoothstreaming
dts			   mpeg1video		      sox
dv			   mpeg2dvd		      spdif
eac3			   mpeg2svcd		      spx
f4v			   mpeg2video		      srt
ffmetadata		   mpeg2vob		      stream_segment
fifo			   mpegts		      sup
fifo_test		   mpjpeg		      swf
filmstrip		   mxf			      tee
fits			   mxf_d10		      tg2
flac			   mxf_opatom		      tgp
flv			   null			      truehd
framecrc		   nut			      tta
framehash		   oga			      uncodedframecrc
framemd5		   ogg			      vc1
g722			   ogv			      vc1t
g723_1			   oma			      voc
g726			   opus			      w64
g726le			   pcm_alaw		      wav
gif			   pcm_f32be		      webm
gsm			   pcm_f32le		      webm_chunk
gxf			   pcm_f64be		      webm_dash_manifest
h261			   pcm_f64le		      webp
h263			   pcm_mulaw		      webvtt
h264			   pcm_s16be		      wtv
hash			   pcm_s16le		      wv
hds			   pcm_s24be		      yuv4mpegpipe
hevc

Enabled protocols:
async			   http			      rtmpt
cache			   httpproxy		      rtp
concat			   icecast		      srtp
crypto			   md5			      subfile
data			   mmsh			      tcp
ffrtmphttp		   mmst			      tee
file			   pipe			      udp
ftp			   prompeg		      udplite
gopher			   rtmp			      unix
hls

Enabled filters:
abench			   bench		      firequalizer
abitscope		   biquad		      flanger
acompressor		   bitplanenoise	      floodfill
acontrast		   blackdetect		      format
acopy			   blend		      fps
acrossfade		   bwdif		      framepack
acrusher		   cellauto		      framerate
adelay			   channelmap		      framestep
adrawgraph		   channelsplit		      gblur
aecho			   chorus		      gradfun
aemphasis		   chromakey		      haas
aeval			   ciescope		      haldclut
aevalsrc		   codecview		      haldclutsrc
afade			   color		      hdcd
afftfilt		   colorbalance		      headphone
afifo			   colorchannelmixer	      hflip
afir			   colorkey		      highpass
aformat			   colorlevels		      hilbert
agate			   colorspace		      histogram
ahistogram		   compand		      hqx
aiir			   compensationdelay	      hstack
ainterleave		   concat		      hue
alimiter		   convolution		      hwdownload
allpass			   convolve		      hwmap
allrgb			   copy			      hwupload
allyuv			   crop			      hysteresis
aloop			   crossfeed		      idet
alphaextract		   crystalizer		      il
alphamerge		   curves		      inflate
amerge			   datascope		      interleave
ametadata		   dcshift		      join
amix			   dctdnoiz		      lenscorrection
amovie			   deband		      life
anequalizer		   decimate		      limiter
anoisesrc		   deconvolve		      loop
anull			   deflate		      loudnorm
anullsink		   deflicker		      lowpass
anullsrc		   dejudder		      lumakey
apad			   deshake		      lut
aperms			   despill		      lut2
aphasemeter		   detelecine		      lut3d
aphaser			   dilation		      lutrgb
apulsator		   displace		      lutyuv
arealtime		   doubleweave		      mandelbrot
aresample		   drawbox		      maskedclamp
areverse		   drawgraph		      maskedmerge
aselect			   drawgrid		      mcompand
asendcmd		   drmeter		      mergeplanes
asetnsamples		   dynaudnorm		      mestimate
asetpts			   earwax		      metadata
asetrate		   ebur128		      midequalizer
asettb			   edgedetect		      minterpolate
ashowinfo		   elbg			      mix
asidedata		   entropy		      movie
asplit			   equalizer		      negate
astats			   erosion		      nlmeans
astreamselect		   extractplanes	      noformat
atadenoise		   extrastereo		      noise
atempo			   fade			      normalize
atrim			   fftfilt		      null
avectorscope		   field		      nullsink
avgblur			   fieldhint		      nullsrc
bandpass		   fieldmatch		      oscilloscope
bandreject		   fieldorder		      overlay
bass			   fifo			      pad
bbox			   fillborders		      palettegen
paletteuse		   setsar		      swapuv
pan			   settb		      tblend
perms			   showcqt		      telecine
pixdesctest		   showfreqs		      testsrc
pixscope		   showinfo		      testsrc2
premultiply		   showpalette		      threshold
prewitt			   showspectrum		      thumbnail
pseudocolor		   showspectrumpic	      tile
psnr			   showvolume		      tlut2
qp			   showwaves		      tonemap
random			   showwavespic		      transpose
readeia608		   shuffleframes	      treble
readvitc		   shuffleplanes	      tremolo
realtime		   sidechaincompress	      trim
remap			   sidechaingate	      unpremultiply
removegrain		   sidedata		      unsharp
removelogo		   signalstats		      vectorscope
replaygain		   silencedetect	      vflip
reverse			   silenceremove	      vfrdet
rgbtestsrc		   sine			      vibrato
roberts			   smptebars		      vignette
rotate			   smptehdbars		      vmafmotion
scale			   sobel		      volume
scale2ref		   spectrumsynth	      volumedetect
select			   split		      vstack
selectivecolor		   ssim			      w3fdif
sendcmd			   stereotools		      waveform
separatefields		   stereowiden		      weave
setdar			   streamselect		      xbr
setfield		   superequalizer	      yadif
setpts			   surround		      yuvtestsrc
setrange		   swaprect		      zoompan

Enabled bsfs:
aac_adtstoasc		   hapqa_extract	      mpeg4_unpack_bframes
chomp			   hevc_metadata	      noise
dca_core		   hevc_mp4toannexb	      null
dump_extradata		   imx_dump_header	      remove_extradata
eac3_core		   mjpeg2jpeg		      text2movsub
extract_extradata	   mjpega_dump_header	      trace_headers
filter_units		   mov2textsub		      vp9_raw_reorder
h264_metadata		   mp3_header_decompress      vp9_superframe
h264_mp4toannexb	   mpeg2_metadata	      vp9_superframe_split
h264_redundant_pps

Enabled indevs:
fbdev			   oss			      v4l2
lavfi

Enabled outdevs:
fbdev			   oss			      v4l2

License: LGPL version 2.1 or later

WARNING: The --disable-yasm option is only provided for compatibility and will be
 removed in the future. Use --enable-x86asm / --disable-x86asm instead.
WARNING: pkg-config not found, library detection may fail.

```



```

arm-linux-androideabi-gcc is unable to create an executable file.


先执行 ./configure ，清除下缓存吧

```



## 编译生成的文件
 - components
  
    decoders
    encoders
    parsers
    demuxers
    muxers
    protocols
    filters
    bsfs
    indevs
    outdevs
    

 -  libraries
 -  programs
  
    ffmpeg  
    ffplay 
    ffprobe 



  ## 相关多媒体文件下载

  [sample video](https://www.sample-videos.com/)

  

sudo ./ffmpeg  -re -i SampleVideo_1280x720_1mb.mp4 -c copy -f h264 rtmp://localhost:2016/live/film

sudo ./ffmpeg  -re -i SampleVideo_1280x720_1mb.mp4 -c copy -f h264 rtmp://localhost:2016/hls_alic/film

## 编译ffplay

必须手动下载[ SDL2.0 ](www.libsdl.org/download-2.0.php)进行编译（configura & make &make install）


## 测试代码
使用 Mumu模拟器进行测试
```
adb push buddy.mp4 adb push buddy.mp4 /storage/emulated/0

```

项目中添加权限
```
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
```

测试ffmpeg的c代码

```
extern "C"
JNIEXPORT void JNICALL
Java_edu_ptu_ffmpeg_ffmpegclang_MainActivity_decode(JNIEnv *env, jclass type, jstring input_,
                                                     jstring output_) {
    unsigned int ver = avcodec_version();
    const char *config  = avcodec_configuration();
    const char *license = avcodec_license();

    const char *input = env->GetStringUTFChars(input_, 0);
    FFLOGE("%s",config);
    //1.注册所有组件
    av_register_all();

    //封装格式上下文，统领全局的结构体，保存了视频文件封装格式的相关信息
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    FFLOGE("%s",input);
    //2.打开输入视频文件
    if (avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0)
    {
        FFLOGE("%s","无法打开输入视频文件");
        return;
    }

    //3.获取视频文件信息
    if (avformat_find_stream_info(pFormatCtx,NULL) < 0)
    {
        FFLOGE("%s","无法获取视频文件信息");
        return;
    }
    FFLOGE("%s","加载成功");
}

```

以下是编译成功so并且能正确执行so的日志
```
10-28 20:48:20.889 9541-9541/edu.ptu.ffmpeg.ffmpegclang E/ffmpeg: --logfile=/home/anshu/workspace/ffmpeg-4.0.2/build-ffmpeg/armeabi-v7a/build-configure.txt --target-os=linux --arch=arm --cpu=cortex-a8 --cross-prefix=arm-linux-androideabi- --sysroot=/home/anshu/workspace/toolchain/sysroot --prefix=/home/anshu/workspace/ffmpeg-4.0.2/build-ffmpeg/armeabi-v7a --enable-gpl --disable-static --enable-shared --disable-programs --disable-doc --enable-yasm --disable-debug --extra-cflags='-march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16' --extra-ldflags='-march=armv7-a -Wl,--fix-cortex-a8'
10-28 20:48:20.893 9541-9541/edu.ptu.ffmpeg.ffmpegclang E/ffmpeg: /storage/emulated/0/buddy.mp4
10-28 20:48:21.317 9541-9541/edu.ptu.ffmpeg.ffmpegclang E/ffmpeg: 加载成功

```
## NGNIX 作为RTMP服务器
[WINDOWS 下 继承RTMP功能的NGNIX](https://github.com/illuspas/nginx-rtmp-win32)
[VLC 播放网络流](https://www.videolan.org/)
[VirtualBox 网络设置为桥接模式](https://www.virtualbox.org/)
[Ffmpeg 推流](https://ffmpeg.zeranoe.com/builds/win64/shared/) 
1.将摄像头推流到hls（Windows）
```
./ffmpeg -f vfwcap  -i "0" -c:v libx264 -preset ultrafast  -acodec libmp3lame -ar 44100 -ac 1  -f flv rtmp://192.168.2.12:1935/hls/home
```
2.将视频文件推流到hls （Linux）

```
ffmpeg -re -i ./big_buck_bunny.mp4 -vcodec copy -acodec copy -f flv rtmp://192.168.2.12:1935/hls/stream
```
3.将屏幕推流到rtmp（Windows）
```
ffmpeg -f gdigrab -i desktop -vcodec libx264 -preset ultrafast -acodec libmp3lame -ar 44100 -ac 1 -f flv rtmp://localhost:1935/live/screen
```
4.将视频文件推流到rtmp（Linux）
```
ffmpeg.exe -re -i ./big_buck_bunny.mp4 -vcodec copy -acodec copy -f flv rtmp://localhost:1935/live/stream

ffmpeg.exe -re -i d://test1-t.ts -vcodec copy -acodec copy -f flv rtmp://localhost:1935/live/stream
```

[ffprobe]

查看视频流信息

ffprobe -v quiet -show_streams  -select_streams v   -i  w.mp4

查看音频流信息

ffprobe -v quiet -show_streams  -select_streams a   -i  w.mp4 

[点播与直播协议介绍文章](https://blog.csdn.net/caoshangpa/article/details/79543916)
[视频样本](https://archive.blender.org/features-gallery/movies/)
[模板视频](https://www.mediacollege.com/adobe/flash/video/tutorial/example-flv.html)

## [经典教程](http://dranger.com/ffmpeg/)
[ffmpeg doxygen](https://ffmpeg.org/doxygen/trunk/examples.html)