## 项目描述
本项目主要用于播放AudioRecord录制的pcm数据文件，
文件可以放在资产目录，也可以放在SDCard上
### 放在SDCard上，请修改代码
请修改MainActivity.java loadData函数

	  private void loadData() {
	        fixedThreadPool.submit(() -> {
	            try {
	                // pcm是采样率32000 双声道 采样位数16位+
	                String path =  "/sdcard/1678788405459.pcm";
	                InputStream in = new FileInputStream(path);
	//        AssetManager assetManager = getAssets();
	//                InputStream in = assetManager.open("one.pcm");
	                int n = 0;
	                while (true) {
	                    byte[] buffer = new byte[32000 * 2 * 2];
	                    n = in.read(buffer);
	                    if (n == -1) {
	                        break;
	                    }
	                    Data data = new Data(buffer, n);
	                    deque1.add(data);
	                    deque2.add(data);
	                }
	                in.close();
	                Log.d(TAG, "loadData read done!");
	            } catch (Exception e) {
	                e.printStackTrace();
	                Log.e(TAG, "loadData Exception ", e);
	            }
	        });
	    }

- String path =  "/sdcard/1678788405459.pcm"; 放在了SDCard根目录，文件是1678788405459.pcm

### 放在资产目录下，请按照以下代码修改
请修改MainActivity.java loadData函数

	  private void loadData() {
		        fixedThreadPool.submit(() -> {
		            try {
		                // pcm是采样率32000 双声道 采样位数16位+
		         
		        AssetManager assetManager = getAssets();
		                InputStream in = assetManager.open("one.pcm");
		                int n = 0;
		                while (true) {
		                    byte[] buffer = new byte[32000 * 2 * 2];
		                    n = in.read(buffer);
		                    if (n == -1) {
		                        break;
		                    }
		                    Data data = new Data(buffer, n);
		                    deque1.add(data);
		                    deque2.add(data);
		                }
		                in.close();
		                Log.d(TAG, "loadData read done!");
		            } catch (Exception e) {
		                e.printStackTrace();
		                Log.e(TAG, "loadData Exception ", e);
		            }
		        });
		    }

-        InputStream in = assetManager.open("one.pcm"); 资产目录下的one.pcm,需要改成你自己的pcm文件名

## 项目功能
### OpenSLES播放
运用Android底层OpenSLEs播放pcm数据，通过JNI调用
### AudioTrack播放
使用AudioTrack播放PCM数据
### OpenSLEs开始录音
运用Android底层OpenSLEs进行录音，通过JNI调用
### OpenSlEs结束录音
结束录音