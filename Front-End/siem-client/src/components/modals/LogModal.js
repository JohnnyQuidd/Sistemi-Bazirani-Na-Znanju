import React from "react";
import Modal from "react-modal";
import '../style/LogModal.css'

Modal.setAppElement('#root')
function LogModal({ log, isModalOpen, setIsModalOpen }) {
  const style = {
    overlay: {
      background: "rgba(44, 42, 42, 0.95)",
    },
    content: {
      width: "50%",
      top: "30%",
      left: "50%",
      right: "auto",
      bottom: "auto",
      marginRight: "-50%",
      transform: "translate(-50%, -50%)",
    },
  };
  return (
    <Modal
      isOpen={isModalOpen}
      onRequestClose={() => setIsModalOpen(false)}
      style={style}
      isOpen={isModalOpen}
    >
      {log &&
        <div className="log-wrapper">
          <div className="log-col-1">
            <p> <b>ID:</b> {log.id}</p>
            <p> <b>Message:</b> {log.message}</p>
            <p> <b>Timestamp:</b> {log.timestamp}</p>
            <p> <b>Type:</b> {log.logType}</p>
            <p> <b>Status:</b> {log.factStatus}</p>
          </div>
          <div className="log-col-2">
            <p> <b>Device ip:</b> {log.device.ipAddress}</p>
            <p> <b>Is device malicious:</b> {log.device.malicious ? 'Yes' : 'No'}</p>
            <p> <b>Software:</b> {log.software.name}</p>
            <p> <b>Platform:</b> {log.os.name}</p>
            <p> <b>User:</b> {log.user.username}</p>
          </div>
        </div>
      }
    </Modal>
  );
}

export default LogModal;
