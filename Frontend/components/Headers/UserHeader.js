import React from "react";

// reactstrap components
import { Button, Container, Row, Col } from "reactstrap";

function UserHeader() {
  return (
    <>
      <div
        className="header pb-0 pt-5 pt-lg-8 d-flex align-items-center"
        style={{
          minHeight: "200px",
          backgroundColor: "rgb(142 183 241)",
          // backgroundImage:
          //   "url(" + require("assets/img/theme/profile-cover.jpg") + ")",
          backgroundSize: "cover",
          backgroundPosition: "center top",
        }}
      ></div>
    </>
  );
}

export default UserHeader;
