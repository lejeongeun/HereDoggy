import { useEffect } from "react";

function useSseNotifications(onNotify) {
  useEffect(() => {
    const es = new EventSource("/api/notifications/subscribe");
    es.addEventListener("notification", () => {
      onNotify();
    });
    return () => es.close();
  }, [onNotify]);
}

export default useSseNotifications;
