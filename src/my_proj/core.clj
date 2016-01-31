(ns my-proj.core
  (:require [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer :all]
            [compojure.route :as route]
            [slim.core :refer [render-template]]))

(defn read-template [filename]
  (slurp (clojure.java.io/resource filename)))

(defroutes myapp
  (GET "/" req (str "Hello World v" (:app-version req)))
  (GET "/hello/:name" [name]
    (render-template "resources/templates/hello.slim" {:name name}))
  (GET "/hi" req
    (let [n (get (:params req) :name)]
      (str "Hello " n "!")))
  (route/resources "/"))

(defn wrap-version [handler]
  (fn [request]
    (handler (assoc request :app-version "1.0.0"))))

(defn -main []
  (run-server (wrap-defaults myapp site-defaults) {:port 5000}))
