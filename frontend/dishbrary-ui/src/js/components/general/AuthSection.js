import React from "react";

import Typography from "@material-ui/core/es/Typography/Typography";

import applicationState from "../../ApplicationState";

export default function AuthSection(props) {
    const {children, unauthorizedMessage} = props;

    return (
        <React.Fragment>
            {
                applicationState.isUserAuthenticated
                    ?
                    children
                    :
                    unauthorizedMessage ? <Typography>{unauthorizedMessage}</Typography> : ""
            }
        </React.Fragment>
    );
}