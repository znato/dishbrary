import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';

import Button from '@material-ui/core/Button';

const styles = theme => ({
    navigationBtnContainer: {
        display: 'table',
        margin: 'auto'
    }
});

class Pagination extends React.Component {

    constructor(props) {
        super(props);
    }

    setActualPage = (newPageNumber) => () => {
        if (typeof this.props.onPageChange === "function") {
            this.props.onPageChange(newPageNumber);
        }
    }

    renderPageButtons = (totalPages, actualPage) => {
        if (totalPages <= 1) {
            return "";
        }

        const lastIndex = totalPages - 1;
        let navigationButtons = [];

        //add actual page button
        navigationButtons.push(
            <Button key={"pageBtn" + actualPage} color="secondary">{actualPage + 1}</Button>
        );

        let leftShiftIndex;
        let rightShiftIndex;
        for (let i = 1; i <= 2; i++) {
            leftShiftIndex = actualPage - i;
            rightShiftIndex = actualPage + i;

            if (leftShiftIndex >= 0) {
                navigationButtons.unshift(
                    <Button key={"pageBtn" + leftShiftIndex} color="primary"
                            onClick={this.setActualPage(leftShiftIndex)}>{leftShiftIndex + 1}</Button>
                );
            }

            if (rightShiftIndex < totalPages) {
                navigationButtons.push(
                    <Button key={"pageBtn" + rightShiftIndex} color="primary"
                            onClick={this.setActualPage(rightShiftIndex)}>{rightShiftIndex + 1}</Button>
                );
            }
        }

        if (leftShiftIndex > 0) {
            let firstPages = [];
            //put maximum 2 page at the beginning
            let i = 0;
            while (i < 2 && i < leftShiftIndex) {
                firstPages.push(
                    <Button key={"pageBtn" + i} color="primary" onClick={this.setActualPage(i)}>{i + 1}</Button>
                );

                i++;
            }

            if (i < leftShiftIndex) {
                firstPages.push(
                    <Button key={"leftPlaceholder"} disabled={true}>...</Button>
                );
            }
            if (firstPages.length > 0) {
                navigationButtons.unshift(firstPages);
            }
        }

        if (rightShiftIndex < lastIndex) {
            let lastPages = [];
            //put maximum 2 pages at the end
            let i = lastIndex;
            while (i > rightShiftIndex && i > lastIndex - 2) {
                lastPages.unshift(
                    <Button key={"pageBtn" + i} color="primary" onClick={this.setActualPage(i)}>{i + 1}</Button>
                );

                i--;
            }
            if (i > rightShiftIndex) {
                lastPages.unshift(
                    <Button key={"rightPlaceholder"} disabled={true}>...</Button>
                );
            }
            if (lastPages.length > 0) {
                navigationButtons.push(lastPages);
            }
        }

        //add previous button as the first element
        navigationButtons.unshift(
            <Button key="prevBtn" color="primary" disabled={actualPage == 0} onClick={this.setActualPage(actualPage - 1)}>&lt;</Button>
        );

        //add next button as the last element
        navigationButtons.push(
            <Button key="nextBtn" color="primary" disabled={actualPage == lastIndex}
                    onClick={this.setActualPage(actualPage + 1)}>&gt;</Button>
        );

        return navigationButtons;
    }

    render() {
        const {classes, children, totalPages, actualPage} = this.props;

        return (
            <div>
                {children}

                <div className={classes.navigationBtnContainer}>
                    {this.renderPageButtons(totalPages, actualPage)}
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(Pagination);