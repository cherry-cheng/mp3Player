# mp3Player
视频mp3项目实现

此小例子中主要演示了从服务器上下载xml文件并解析；下载lrc文件，使用正则表达式过滤歌词中的时间和内容信息，并存放在Queue中；下载MP3文件并播放。歌曲播放放在Service中
其中Mp3文件的播放和歌词的同步做了两种可能：1、在Activity中利用Handler来同步更新歌词。但是这个有缺陷，因为当activity是比较被动的。当次activity不可见时，
不希望它做更多的操作。故而。2、将歌词的同步挪到service当中，结合BroadCast当activity可见时才更新歌词。
