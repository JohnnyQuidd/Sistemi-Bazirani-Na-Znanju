import React from "react";
import Modal from "react-modal";
import "../style/AlarmModal.css";

Modal.setAppElement("#root");
function AlarmModal({ alarm, isModalOpen, setIsModalOpen }) {
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
      {
        alarm &&
        <div className="alarm-wrapper">
        <div className="alarm-col-1">
              <p> <b>ID:</b> {alarm.id}</p>
              <p> <b>Message:</b> {alarm.message}</p>
              <p> <b>Timestamp:</b> {alarm.timestamp}</p>
              <p> <b>Status:</b> {alarm.factStatus}</p>
            </div>
            <div className="alarm-col-2">
            { alarm.relatedLogs && <p> <b>Device IP:</b> {alarm.relatedLogs[0].device.ipAddress}</p> }
              { alarm.relatedLogs && <p> <b>Log that invoked the alarm:</b> {alarm.relatedLogs[0].message}</p> }
              { alarm.relatedUsers && <p> <b>User that invoked the alarm:</b> {alarm.relatedUsers[0].username}</p> }
            </div>
        </div>
      }
    </Modal>
  );
}

export default AlarmModal;
