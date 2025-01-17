import path from "path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  build: {
    rollupOptions: {
      output: {
        // 控制入口文件的命名规则
        entryFileNames: "assets/[name].js",
        // 控制代码分割生成的 chunk 文件的命名规则
        chunkFileNames: "assets/[name].js",
        // 控制静态资源文件（如图片、字体等）的命名规则
        assetFileNames: "assets/[name].[ext]",
        // 手动控制代码拆分
        manualChunks: {
          react: ["react", "react-dom"],
        },
      },
    },
  },
});
